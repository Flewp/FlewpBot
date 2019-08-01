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
import net.engio.mbassy.listener.Handler;
import okhttp3.OkHttpClient;
import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.defaults.element.DefaultUser;
import org.kitteh.irc.client.library.element.MessageTag;
import org.kitteh.irc.client.library.event.client.ClientReceiveCommandEvent;
import org.kitteh.irc.client.library.feature.filter.CommandFilter;
import org.kitteh.irc.client.library.feature.twitch.TwitchSupport;
import org.kitteh.irc.client.library.feature.twitch.event.UserNoticeEvent;
import org.slf4j.LoggerFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class TwitchAPIController {
    private Configuration configuration;
    private EventManager eventManager;
    private TwitchKrakenAPI twitchKrakenAPI;
    private TwitchHelixAPI twitchHelixAPI;

    private boolean joinedChatRooms;
    private List<KrakenChatRoom> chatRoomList = new ArrayList<>();
    private String channelId;
    private String streamerUserId;

    private ExecutorService botExecutorService;
    private Client ircClient;

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

        ircClient = Client.builder()
                .server().host("irc.chat.twitch.tv").port(443)
                .password(configuration.twitchChatBotAccessToken).then()
                .nick(configuration.twitchChatBotName)
                .build();

        TwitchSupport.addSupport(ircClient);

        joinedChatRooms = false;
    }

    public synchronized void startChatBot() {
        try {
            determineChatRoomList();

            ircClient.getEventManager().registerEventListener(this);
            ircClient.addChannel("#" + configuration.twitchStreamerName);
            for (KrakenChatRoom chatRoom : chatRoomList) {
                ircClient.addChannel("#chatrooms:" + channelId + ":" + chatRoom._id);
            }

            botExecutorService.submit(() -> {
                try {
                    ircClient.connect();
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

    @CommandFilter("PRIVMSG")
    @Handler
    public void onMessage(ClientReceiveCommandEvent event) {
        if (event.getParameters().size() != 2) {
            return;
        }

        Optional<MessageTag> bits = event.getTag("bits");
        int bitsInt = -1;
        if (bits.isPresent() && bits.get().getValue().isPresent()) {
            try {
                bitsInt = Integer.parseInt(bits.get().getValue().get());

            } catch (Exception e) {

            }
        }

        if (bitsInt > 0) {
            eventManager.dispatchEvent(new BitEvent(new EventUser(messageTagsToMap(event.getTags()),
                    ((DefaultUser) event.getActor()).getNick()), event.getParameters().get(1), bitsInt));
        } else {
            String chatRoomId = event.getParameters().get(0).substring(event.getParameters().get(0).indexOf(":") + 1);
            KrakenChatRoom chatRoom = chatRoomList.stream().filter(room ->
                    room._id.equals(chatRoomId)).findFirst().orElse(null);
            eventManager.dispatchEvent(new ChatEvent(new EventUser(messageTagsToMap(event.getTags()),
                    ((DefaultUser) event.getActor()).getNick()), chatRoom,
                    chatRoomId, event.getParameters().get(0), event.getParameters().get(1)));
        }
    }

    @Handler
    public void onWhisper(org.kitteh.irc.client.library.feature.twitch.event.WhisperEvent event) {
        eventManager.dispatchEvent(new WhisperEvent(new EventUser(messageTagsToMap(event.getTags()),
                event.getActor().getNick()), event.getTarget(), event.getMessage()));

        LoggerFactory.getLogger(TwitchAPIController.class).info(event.toString());
    }

    @Handler
    public void onUserNotice(UserNoticeEvent event) {
        String login = "";
        if (event.getTag("login").isPresent() && event.getTag("login").get().getValue().isPresent()) {
            login = event.getTag("login").get().getValue().get();
        }

        SubscribeEvent subscribeEvent = SubscribeEvent.parse(messageTagsToMap(event.getTags()),
                event.getMessage().orElse(""), login);

        if (subscribeEvent != null) {
            eventManager.dispatchEvent(subscribeEvent);
        }
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

    public Client getIrcClient() {
        return ircClient;
    }

    private Map<String, String> messageTagsToMap(List<MessageTag> tags) {
        return tags.stream().collect(Collectors.toMap(MessageTag::getName, o -> o.getValue().orElse("")));
    }
}
