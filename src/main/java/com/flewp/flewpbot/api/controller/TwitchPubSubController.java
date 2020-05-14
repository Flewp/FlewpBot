package com.flewp.flewpbot.api.controller;

import com.flewp.flewpbot.Configuration;
import com.flewp.flewpbot.EventManager;
import com.flewp.flewpbot.api.JamisphereAPI;
import com.flewp.flewpbot.api.TwitchPubsubAPI;
import com.flewp.flewpbot.model.events.twitch.pubsub.ListenData;
import com.flewp.flewpbot.model.events.twitch.pubsub.PubsubEvent;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tinder.scarlet.Lifecycle;
import com.tinder.scarlet.ShutdownReason;
import com.tinder.scarlet.Stream;
import com.tinder.scarlet.WebSocket;
import com.tinder.scarlet.lifecycle.LifecycleRegistry;
import org.jetbrains.annotations.NotNull;

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
                System.out.println("Wow");
            }

            @Override
            public void onComplete() {
                System.out.println("Wow");
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
                        System.out.println("PONG");
                        break;
                }

            }

            @Override
            public void onError(@NotNull Throwable throwable) {
                System.out.println("Wow");
            }

            @Override
            public void onComplete() {
                System.out.println("Wow");
            }
        });

        currentPingTimestamp = System.currentTimeMillis();
        currentPongTimestamp = System.currentTimeMillis();

        webSocketExecutorService = Executors.newScheduledThreadPool(2);
        webSocketExecutorService.scheduleAtFixedRate(() -> {
            System.out.println("PING");
            currentPingTimestamp = System.currentTimeMillis();
            twitchPubsubAPI.sendEvent(new PubsubEvent("PING", "", new JsonObject()));
        }, 3, 3, TimeUnit.SECONDS);
        webSocketExecutorService.scheduleAtFixedRate(() -> {
            if (currentPongTimestamp > currentPingTimestamp) {
                System.out.println("We're healthy!");
                return;
            }

            if (Duration.between(Instant.ofEpochMilli(currentPingTimestamp), Instant.now()).toMillis() <
                    Duration.ofSeconds(10).toMillis()) {
                System.out.println("Waiting for pong...");
                return;
            }


            // Restart
            new Thread(this::restartWebSocket).start();

        }, 1, 1, TimeUnit.SECONDS);

        lifecycleRegistry.onNext(Lifecycle.State.Started.INSTANCE);
    }
}
