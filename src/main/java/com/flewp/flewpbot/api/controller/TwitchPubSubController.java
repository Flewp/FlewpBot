package com.flewp.flewpbot.api.controller;

import com.flewp.flewpbot.Configuration;
import com.flewp.flewpbot.EventManager;
import com.flewp.flewpbot.api.JamisphereAPI;
import com.flewp.flewpbot.api.TwitchPubsubAPI;
import com.flewp.flewpbot.model.events.jamisphere.PointsRedeemedEvent;
import com.flewp.flewpbot.model.events.twitch.pubsub.ListenData;
import com.flewp.flewpbot.model.events.twitch.pubsub.PubsubEvent;
import com.flewp.flewpbot.model.events.twitch.pubsub.PubsubMessage;
import com.flewp.flewpbot.model.events.twitch.pubsub.RedemptionData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tinder.scarlet.Lifecycle;
import com.tinder.scarlet.ShutdownReason;
import com.tinder.scarlet.Stream;
import com.tinder.scarlet.WebSocket;
import com.tinder.scarlet.lifecycle.LifecycleRegistry;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TwitchPubSubController {
    private Gson gson = new Gson();

    private Configuration configuration;
    private EventManager eventManager;
    private LifecycleRegistry lifecycleRegistry;
    private TwitchPubsubAPI twitchPubsubAPI;
    private JamisphereAPI jamisphereAPI;

    private Logger logger;

    private Stream.Disposable websocketEventDisposable;
    private Stream.Disposable pubsubEventDisposable;

    private Long currentPingTimestamp;
    private Long currentPongTimestamp;

    private ScheduledExecutorService webSocketExecutorService;

    public TwitchPubSubController(Configuration configuration, EventManager eventManager, LifecycleRegistry lifecycleRegistry,
                                  TwitchPubsubAPI twitchPubsubAPI, JamisphereAPI jamisphereAPI) {
        this.configuration = configuration;
        this.eventManager = eventManager;
        this.lifecycleRegistry = lifecycleRegistry;
        this.twitchPubsubAPI = twitchPubsubAPI;
        this.jamisphereAPI = jamisphereAPI;

        logger = LoggerFactory.getLogger(TwitchPubSubController.class);
    }

    public void startTwitchPubSub() {
        if (!configuration.enablePubSub) {
            return;
        }

        restartWebSocket();
    }


    private void restartWebSocket() {
        if (webSocketExecutorService != null) {
            if (!webSocketExecutorService.isShutdown()) {
                webSocketExecutorService.shutdownNow();
            }

            webSocketExecutorService = null;
        }

        lifecycleRegistry.onNext(new Lifecycle.State.Stopped.WithReason(ShutdownReason.GRACEFUL));

        if (websocketEventDisposable != null && !websocketEventDisposable.isDisposed()) {
            websocketEventDisposable.dispose();
            websocketEventDisposable = null;
        }

        if (pubsubEventDisposable != null && !pubsubEventDisposable.isDisposed()) {
            pubsubEventDisposable.dispose();
            pubsubEventDisposable = null;
        }

        websocketEventDisposable = twitchPubsubAPI.observeWebSocketEvents().start(new Stream.Observer<WebSocket.Event>() {
            @Override
            public void onNext(WebSocket.Event event) {
                if (event instanceof WebSocket.Event.OnConnectionOpened) {
                    ListenData listenData = new ListenData(Collections.singletonList("channel-points-channel-v1." + "104896188"), configuration.twitchStreamerAccessToken);

                    twitchPubsubAPI.sendEvent(new PubsubEvent("LISTEN", "817238917938szdas",
                            gson.toJsonTree(listenData).getAsJsonObject()));
                }
            }

            @Override
            public void onError(@NotNull Throwable throwable) {
                logger.error("Websocket Events On Error " + throwable.toString());
                restartAsync();
            }

            @Override
            public void onComplete() {
                logger.error("Websocket Events On Complete");
            }
        });

        pubsubEventDisposable = twitchPubsubAPI.observePubsubEvents().start(new Stream.Observer<PubsubEvent>() {
            @Override
            public void onNext(PubsubEvent event) {
                if (event.type == null) {
                    return;
                }

                switch (event.type) {
                    case "PONG":
                        currentPongTimestamp = System.currentTimeMillis();
                        logger.info("Websocket PONG");
                        break;
                    case "MESSAGE":
                        if (event.data == null) {
                            return;
                        }

                        logger.info(event.toString());

                        try {
                            PubsubMessage pubsubMessage = gson.fromJson(event.data, PubsubMessage.class);
                            if (pubsubMessage.message == null) {
                                return;
                            }

                            PubsubMessage.Data data = gson.fromJson(pubsubMessage.message, PubsubMessage.Data.class);
                            if (data.type == null || data.data == null || !data.type.equals("reward-redeemed")) {
                                return;
                            }

                            RedemptionData redemptionData = gson.fromJson(data.data, RedemptionData.class);

                            if (redemptionData == null) {
                                return;
                            }

                            eventManager.dispatchEvent(new PointsRedeemedEvent(redemptionData));
                        } catch (Exception e) {

                        }
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onError(@NotNull Throwable throwable) {
                logger.error("Pubsub Events on Error " + throwable.toString());
                restartAsync();
            }

            @Override
            public void onComplete() {
                logger.error("Pubsub Events on Complete");
            }
        });

        currentPingTimestamp = System.currentTimeMillis();
        currentPongTimestamp = System.currentTimeMillis();

        webSocketExecutorService = Executors.newScheduledThreadPool(2);
        webSocketExecutorService.scheduleAtFixedRate(() -> {
            logger.info("Websocket PING");
            currentPingTimestamp = System.currentTimeMillis();
            twitchPubsubAPI.sendEvent(new PubsubEvent("PING", "", new JsonObject()));
        }, 3, 3, TimeUnit.MINUTES);
        webSocketExecutorService.scheduleAtFixedRate(() -> {
            if (currentPongTimestamp >= currentPingTimestamp) {
                return;
            }

            if (Duration.between(Instant.ofEpochMilli(currentPingTimestamp), Instant.now()).toMillis() <
                    Duration.ofSeconds(10).toMillis()) {
                logger.warn("Waiting for pong...");
                return;
            }

            logger.warn("Restarting Websocket...");

            restartAsync();
        }, 3, 3, TimeUnit.SECONDS);

        lifecycleRegistry.onNext(Lifecycle.State.Started.INSTANCE);
        logger.info("PubSub Websocket Connected!");
    }

    private void restartAsync() {
        new Thread(this::restartWebSocket).start();
    }
}
