package com.flewp.flewpbot.api;

import com.flewp.flewpbot.Configuration;
import com.flewp.flewpbot.event.*;
import com.flewp.flewpbot.model.Channel;
import com.flewp.flewpbot.model.ChatRoom;
import com.flewp.flewpbot.model.ChatRoomsResponse;
import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.helix.domain.UserList;
import okhttp3.OkHttpClient;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.Listener;
import org.pircbotx.hooks.events.DisconnectEvent;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.UnknownEvent;
import org.slf4j.LoggerFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TwitchAPIController implements Listener {
    private Configuration configuration;
    private EventManager eventManager;

    private OkHttpClient okHttpClient;
    private TwitchClient twitchClient;
    private TwitchTokenGeneratorAPI generatorAPI;
    private TwitchKrakenAPI krakenAPI;

    private boolean joinedChatRooms;
    private List<ChatRoom> chatRoomList;
    private String channelId;
    private String streamerUserId;

    private ExecutorService botExecutorService;
    private PircBotX pircBotX;

    public TwitchAPIController(Configuration configuration, EventManager eventManager) {
        this.configuration = configuration;
        this.eventManager = eventManager;

        botExecutorService = Executors.newSingleThreadExecutor();

        okHttpClient = new OkHttpClient();

        generatorAPI = new Retrofit.Builder()
                .baseUrl("https://twitchtokengenerator.com/")
                .client(okHttpClient.newBuilder().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(TwitchTokenGeneratorAPI.class);

        pircBotX = new PircBotX(new org.pircbotx.Configuration.Builder()
                .setAutoNickChange(false)
                .setOnJoinWhoEnabled(false)
                .setCapEnabled(true)
                .addServer("irc.twitch.tv")
                .setName(configuration.twitchChatBotName)
                .setServerPassword(configuration.twitchChatBotAccessToken)
                .addAutoJoinChannel("#" + configuration.twitchStreamerName)
                .addListener(this)
                .buildConfiguration());

        joinedChatRooms = false;
    }

    public synchronized void connect() {
        try {
            // Refresh the streamer access token
            Response<RefreshTokenResponse> tokenResponse = generatorAPI
                    .refreshToken(configuration.twitchStreamerRefreshToken).execute();

            if (!tokenResponse.isSuccessful() || tokenResponse.body() == null || !tokenResponse.body().success) {
                throw new IllegalStateException("Can't refresh streamer access token.");
            }

            // Update the configuration with the new access token
            configuration.twitchStreamerAccessToken = tokenResponse.body().token;
            configuration.twitchStreamerRefreshToken = tokenResponse.body().refresh;
            configuration.dumpFile();

            // Build Twitch4J
            twitchClient = TwitchClientBuilder.builder()
                    .withEnableHelix(true)
                    .withClientId(configuration.twitchAppClientID)
                    .withClientSecret(configuration.twitchAppClientSecret)
                    .withEventManager(eventManager)
                    .build();

            // Build Kraken API
            krakenAPI = new Retrofit.Builder()
                    .baseUrl("https://api.twitch.tv/kraken/")
                    .client(okHttpClient.newBuilder()
                            .addInterceptor(new TwitchKrakenRequestInterceptor(configuration.twitchAppClientID, configuration.twitchStreamerAccessToken))
                            .build())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(TwitchKrakenAPI.class);

            // Get user ID from given streamer name
            UserList userList = twitchClient.getHelix().getUsers(configuration.twitchStreamerAccessToken, null,
                    Collections.singletonList(configuration.twitchStreamerName)).execute();

            if (userList == null || userList.getUsers().isEmpty()) {
                throw new IllegalStateException("Can't get streamer ID to query chat rooms");
            }

            streamerUserId = Long.toString(userList.getUsers().get(0).getId());

            Response<Channel> channelResponse = krakenAPI.channel().execute();

            if (!channelResponse.isSuccessful() || channelResponse.body() == null
                    || channelResponse.body().get_id() == null) {
                throw new IllegalStateException("Can't get channel information.");
            }

            channelId = channelResponse.body().get_id();

            // Get chat rooms for streamer
            Response<ChatRoomsResponse> chatRoomsResponse = krakenAPI.chatRooms(streamerUserId).execute();

            if (chatRoomsResponse.isSuccessful() && chatRoomsResponse.body() != null
                    && chatRoomsResponse.body().getRooms() != null) {
                chatRoomList = chatRoomsResponse.body().getRooms();
            } else {
                chatRoomList = new ArrayList<>();
            }

            botExecutorService.submit(() -> {
                try {
                    pircBotX.startBot();
                } catch (Exception e) {
                    LoggerFactory.getLogger(TwitchAPIController.class).error("Error in PircBot", e);
                }
            });

        } catch (Exception e) {
            LoggerFactory.getLogger(TwitchAPIController.class).error("Error in connecting PircBot", e);
        }
    }

    @Override
    public void onEvent(Event event) throws Exception {
        if (event instanceof JoinEvent) {
            if (joinedChatRooms) {
                return;
            }

            joinedChatRooms = true;

            if (chatRoomList != null && !chatRoomList.isEmpty()) {
                for (ChatRoom chatRoom : chatRoomList) {
                    pircBotX.sendRaw().rawLine("JOIN #chatrooms:" + channelId + ":" + chatRoom.get_id());
                }
            }

            pircBotX.sendRaw().rawLine("CAP REQ :twitch.tv/tags twitch.tv/commands");
            LoggerFactory.getLogger(TwitchAPIController.class).info("FlewpBot has successfully connected to chat.");
        } else if (event instanceof MessageEvent) {
            MessageEvent messageEvent = (MessageEvent) event;
            if (messageEvent.getTags() == null || messageEvent.getUser() == null || messageEvent.getUser().getNick() == null) {
                return;
            }

            if (messageEvent.getTags().containsKey("bits")) {
                try {
                    eventManager.dispatchEvent(new BitEvent(new EventUser(messageEvent.getTags(), messageEvent.getUser().getNick()),
                            messageEvent.getMessage(), Integer.parseInt(messageEvent.getTags().get("bits"))));
                } catch (Exception e) {
                    LoggerFactory.getLogger(TwitchAPIController.class).error("Error in parsing bits: ", e);
                }
            } else if (messageEvent.getChannel() != null && messageEvent.getChannel().getName() != null) {
                String chatRoomId = messageEvent.getChannel().getName()
                        .substring(messageEvent.getChannel().getName().lastIndexOf(":") + 1);

                eventManager.dispatchEvent(new ChatEvent(new EventUser(messageEvent.getTags(), messageEvent.getUser().getNick()),
                        chatRoomList.stream().filter(room -> room.get_id().equals(chatRoomId)).findFirst().orElse(null),
                        chatRoomId, messageEvent.getMessage()));
            }
        } else if (event instanceof UnknownEvent) {
            UnknownEvent unknownEvent = (UnknownEvent) event;
            if (unknownEvent.getTags() == null || unknownEvent.getNick() == null || unknownEvent.getParsedLine() == null) {
                return;
            }

            switch (unknownEvent.getCommand()) {
                case "WHISPER":
                    eventManager.dispatchEvent(new WhisperEvent(new EventUser(unknownEvent.getTags(), unknownEvent.getNick()),
                            unknownEvent.getTarget(), unknownEvent.getParsedLine().get(unknownEvent.getParsedLine().size() - 1)));
                    break;
                case "USERNOTICE":
                    SubscribeEvent subscribeEvent = SubscribeEvent.parse(unknownEvent.getTags(),
                            (unknownEvent.getParsedLine() == null || unknownEvent.getParsedLine().isEmpty()) ? "" :
                                    unknownEvent.getParsedLine().get(unknownEvent.getParsedLine().size() - 1), unknownEvent.getTags().get("login"));

                    if (subscribeEvent != null) {
                        eventManager.dispatchEvent(subscribeEvent);
                    }
                    break;
            }
        } else if (event instanceof DisconnectEvent) {
            LoggerFactory.getLogger(TwitchAPIController.class).info("FlewpBot has disconnected from chat.");
        }
    }

    public PircBotX getPircBotX() {
        return pircBotX;
    }

    public TwitchClient getTwitchClient() {
        return twitchClient;
    }

    public TwitchKrakenAPI getTwitchKrakenAPI() {
        return krakenAPI;
    }

    public List<ChatRoom> getChatRoomList() {
        return chatRoomList;
    }

    public String getStreamerUserId() {
        return streamerUserId;
    }

    public String getStreamerChannelId() {
        return channelId;
    }
}
