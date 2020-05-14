package com.flewp.flewpbot.api.controller;

import com.flewp.flewpbot.Configuration;
import com.flewp.flewpbot.EventManager;
import com.flewp.flewpbot.api.JamisphereAPI;
import com.flewp.flewpbot.api.RetrofitEmptyCallback;
import com.flewp.flewpbot.api.TwitchAPI;
import com.flewp.flewpbot.api.TwitchAuthAPI;
import com.flewp.flewpbot.model.api.GetUsersResponse;
import com.flewp.flewpbot.model.api.JamispherePusherBody;
import com.flewp.flewpbot.model.api.TwitchTokenResponse;
import com.flewp.flewpbot.model.events.twitch.*;
import com.google.gson.Gson;
import net.engio.mbassy.listener.Handler;
import okhttp3.OkHttpClient;
import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.defaults.element.DefaultUser;
import org.kitteh.irc.client.library.element.MessageTag;
import org.kitteh.irc.client.library.event.client.ClientReceiveCommandEvent;
import org.kitteh.irc.client.library.event.connection.ClientConnectionClosedEvent;
import org.kitteh.irc.client.library.event.connection.ClientConnectionEndedEvent;
import org.kitteh.irc.client.library.event.connection.ClientConnectionFailedEvent;
import org.kitteh.irc.client.library.feature.filter.CommandFilter;
import org.kitteh.irc.client.library.feature.twitch.TwitchSupport;
import org.kitteh.irc.client.library.feature.twitch.event.UserNoticeEvent;
import org.slf4j.LoggerFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class TwitchAPIController {
    private Gson gson = new Gson();

    private Configuration configuration;
    private EventManager eventManager;
    private TwitchAPI twitchAPI;
    private JamisphereAPI jamisphereAPI;

    private String channelId;
    private String streamerUserId;

    private ExecutorService ircExecutorService;
    private Client ircClient;

    public static void refreshCredentials(Configuration configuration) {
        try {

            TwitchAuthAPI twitchAuthAPI = new Retrofit.Builder()
                    .baseUrl("https://id.twitch.tv/")
                    .client(new OkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(TwitchAuthAPI.class);

            Response<TwitchTokenResponse> tokenResponse = twitchAuthAPI.token(configuration.twitchAppClientID, configuration.twitchAppClientSecret,
                    "refresh_token", configuration.twitchStreamerRefreshToken).execute();

            if (!tokenResponse.isSuccessful() || tokenResponse.body() == null ||
                    tokenResponse.body().access_token == null || tokenResponse.body().refresh_token == null) {
                LoggerFactory.getLogger(TwitchAuthAPI.class).error("Couldn't refresh Twitch API token: " + tokenResponse.message());
                return;
            }

            // Update the configuration with the new access token
            configuration.twitchStreamerAccessToken = tokenResponse.body().access_token;
            configuration.twitchStreamerRefreshToken = tokenResponse.body().refresh_token;
            configuration.dumpFile();
        } catch (Exception e) {
            LoggerFactory.getLogger(TwitchAPIController.class).error("Couldn't refresh Twitch credentials: " + e.getMessage());
        }
    }

    public TwitchAPIController(Configuration configuration, EventManager eventManager, TwitchAPI twitchAPI, JamisphereAPI jamisphereAPI) {
        this.configuration = configuration;
        this.eventManager = eventManager;
        this.twitchAPI = twitchAPI;
        this.jamisphereAPI = jamisphereAPI;
    }

    public synchronized void startChatBot() {
        try {
            ircExecutorService = Executors.newSingleThreadExecutor();

            ircClient = Client.builder()
                    .server().host("irc.chat.twitch.tv").port(443)
                    .password(configuration.twitchChatBotAccessToken).then()
                    .nick(configuration.twitchChatBotName)
                    .build();

            TwitchSupport.addSupport(ircClient);

            // Get user ID from given streamer name
            Response<GetUsersResponse> getUsersResponse = twitchAPI.getUsers(null,
                    Collections.singletonList(configuration.twitchStreamerName)).execute();

            if (!getUsersResponse.isSuccessful() || getUsersResponse.body() == null ||
                    getUsersResponse.body().data == null || getUsersResponse.body().data.isEmpty()) {
                LoggerFactory.getLogger(TwitchAPIController.class).error("Can't get streamer ID");
                return;
            }

            streamerUserId = getUsersResponse.body().data.get(0).id;

            // This may change down the road, but as far as Twitch is concerned, these are the same.
            channelId = streamerUserId;

            ircClient.getEventManager().registerEventListener(this);
            ircClient.addChannel("#" + configuration.twitchStreamerName);

            ircExecutorService.submit(() -> {
                try {
                    ircClient.connect();
                } catch (Exception e) {
                    LoggerFactory.getLogger(TwitchAPIController.class).error("Error in Kitteh", e);
                }
            });

        } catch (Exception e) {
            LoggerFactory.getLogger(TwitchAPIController.class).error("Error in connecting Kitteh", e);
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
            BitEvent bitEvent = new BitEvent(new EventUser(messageTagsToMap(event.getTags()),
                    ((DefaultUser) event.getActor()).getNick()), event.getParameters().get(1), bitsInt);
            jamisphereAPI.pusher(new JamispherePusherBody(gson.toJsonTree(bitEvent).getAsJsonObject(), "jamisphere", "twitchCheer")).enqueue(new RetrofitEmptyCallback<>());
            eventManager.dispatchEvent(bitEvent);
        } else {
            String chatRoomId = event.getParameters().get(0).substring(event.getParameters().get(0).indexOf(":") + 1);
            eventManager.dispatchEvent(new ChatEvent(new EventUser(messageTagsToMap(event.getTags()),
                    ((DefaultUser) event.getActor()).getNick()), chatRoomId, event.getParameters().get(1)));
        }
    }

    @Handler
    public void onWhisper(org.kitteh.irc.client.library.feature.twitch.event.WhisperEvent event) {
        eventManager.dispatchEvent(new ChatEvent(MessageOrigin.TwitchWhisper, new EventUser(messageTagsToMap(event.getTags()),
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
            jamisphereAPI.pusher(new JamispherePusherBody(gson.toJsonTree(subscribeEvent).getAsJsonObject(), "jamisphere", "twitchSubscribe")).enqueue(new RetrofitEmptyCallback<>());
            eventManager.dispatchEvent(subscribeEvent);
        }
    }

    @Handler
    public void onDisconnect(ClientConnectionClosedEvent event) {
        LoggerFactory.getLogger(TwitchAPIController.class).info(event.toString());
    }

    @Handler
    public void onDisconnect(ClientConnectionEndedEvent event) {
        LoggerFactory.getLogger(TwitchAPIController.class).info(event.toString());
    }

    @Handler
    public void onDisconnect(ClientConnectionFailedEvent event) {
        LoggerFactory.getLogger(TwitchAPIController.class).info(event.toString());
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
