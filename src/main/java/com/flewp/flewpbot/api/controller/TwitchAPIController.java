package com.flewp.flewpbot.api.controller;

import com.flewp.flewpbot.Configuration;
import com.flewp.flewpbot.api.TwitchHelixAPI;
import com.flewp.flewpbot.api.TwitchKrakenAPI;
import com.flewp.flewpbot.api.TwitchTokenGeneratorAPI;
import com.flewp.flewpbot.event.*;
import com.flewp.flewpbot.model.api.ChatRoomsResponse;
import com.flewp.flewpbot.model.api.GetUsersResponse;
import com.flewp.flewpbot.model.api.RefreshTokenResponse;
import com.flewp.flewpbot.model.kraken.KrakenChatRoom;
import com.github.philippheuer.events4j.EventManager;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TwitchAPIController implements Listener {
    private Configuration configuration;
    private EventManager eventManager;
    private TwitchKrakenAPI twitchKrakenAPI;
    private TwitchHelixAPI twitchHelixAPI;

    private boolean joinedChatRooms;
    private List<KrakenChatRoom> chatRoomList = new ArrayList<>();
    private String channelId;
    private String streamerUserId;

    private ExecutorService botExecutorService;
    private PircBotX pircBotX;

    public static void refreshCredentials(Configuration configuration) {
        try {
            TwitchTokenGeneratorAPI generatorAPI = new Retrofit.Builder()
                    .baseUrl("https://twitchtokengenerator.com/")
                    .client(new OkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(TwitchTokenGeneratorAPI.class);

            // Refresh the streamer access token
            Response<RefreshTokenResponse> tokenResponse = generatorAPI
                    .refreshToken(configuration.twitchStreamerRefreshToken).execute();

            if (!tokenResponse.isSuccessful() || tokenResponse.body() == null || !tokenResponse.body().success) {
                throw new IllegalStateException("Bad response from Token API");
            }

            // Update the configuration with the new access token
            configuration.twitchStreamerAccessToken = tokenResponse.body().token;
            configuration.twitchStreamerRefreshToken = tokenResponse.body().refresh;
            configuration.dumpFile();
        } catch (Exception e) {
            LoggerFactory.getLogger(TwitchAPIController.class).error("Couldn't refresh Twitch credentials: " + e.getMessage());
        }
    }

    public TwitchAPIController(Configuration configuration, EventManager eventManager, TwitchKrakenAPI twitchKrakenAPI, TwitchHelixAPI twitchHelixAPI) {
        this.configuration = configuration;
        this.eventManager = eventManager;
        this.twitchKrakenAPI = twitchKrakenAPI;
        this.twitchHelixAPI = twitchHelixAPI;

        botExecutorService = Executors.newSingleThreadExecutor();

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

    public synchronized void startChatBot() {
        try {
            determineChatRoomList();

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

    private void determineChatRoomList() throws Exception {
        // Get user ID from given streamer name
        Response<GetUsersResponse> getUsersResponse = twitchHelixAPI.getUsers(null,
                Collections.singletonList(configuration.twitchStreamerName)).execute();

        if (!getUsersResponse.isSuccessful() || getUsersResponse.body() == null ||
                getUsersResponse.body().data == null || getUsersResponse.body().data.isEmpty()) {
            LoggerFactory.getLogger(TwitchAPIController.class).error("Can't get streamer ID to query chat rooms");
            return;
        }

        streamerUserId = getUsersResponse.body().data.get(0).id;

        // This may change down the road, but as far as Kraken is concerned, these are the same.
        channelId = streamerUserId;

        // Get chat rooms for streamer
        Response<ChatRoomsResponse> chatRoomsResponse = twitchKrakenAPI.chatRooms(streamerUserId).execute();

        if (chatRoomsResponse.isSuccessful() && chatRoomsResponse.body() != null
                && chatRoomsResponse.body().getRooms() != null) {
            chatRoomList = chatRoomsResponse.body().getRooms();
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
                for (KrakenChatRoom chatRoom : chatRoomList) {
                    pircBotX.sendRaw().rawLine("JOIN #chatrooms:" + channelId + ":" + chatRoom._id);
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
                        chatRoomList.stream().filter(room -> room._id.equals(chatRoomId)).findFirst().orElse(null),
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

    public List<KrakenChatRoom> getChatRoomList() {
        return chatRoomList;
    }

    public String getStreamerUserId() {
        return streamerUserId;
    }

    public String getStreamerChannelId() {
        return channelId;
    }
}
