package com.flewp.flewpbot.pusher;

import com.flewp.flewpbot.Configuration;
import com.flewp.flewpbot.EventManager;
import com.flewp.flewpbot.model.events.jamisphere.*;
import com.flewp.flewpbot.model.events.twitch.BitEvent;
import com.flewp.flewpbot.model.events.twitch.NewDonationEvent;
import com.flewp.flewpbot.model.events.twitch.SubscribeEvent;
import com.google.gson.Gson;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class PusherManager {
    private static final String JAMISPHERE_EVENT = "jamisphere";
    private Gson gson = new Gson();
    private Pusher pusher;

    private Configuration configuration;
    private EventManager eventManager;

    private Channel channel;
    private ConnectionEventListener connectionEventListener;

    private boolean reconnect = true;

    @Inject
    public PusherManager(Configuration configuration, EventManager eventManager) {
        this.configuration = configuration;
        this.eventManager = eventManager;
    }

    public void connect() {
        if (configuration.pusherCluster == null || configuration.pusherKey == null) {
            LoggerFactory.getLogger(PusherManager.class).info("Pusher credentials not provided, Pusher will not be connected.");
            return;
        }

        if (pusher != null && (pusher.getConnection().getState() == ConnectionState.CONNECTING
                || pusher.getConnection().getState() == ConnectionState.CONNECTED)) {
            // Don't try to connect if we already have a connected pusher client.
            return;
        }

        PusherOptions options = new PusherOptions().setCluster(configuration.pusherCluster);
        pusher = new Pusher(configuration.pusherKey, options);

        channel = pusher.subscribe(JAMISPHERE_EVENT);
        connectionEventListener = new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                LoggerFactory.getLogger(PusherManager.class).info("Pusher connection changed from \""
                        + change.getPreviousState().name() + "\" to \"" + change.getCurrentState().name() + "\"");

                if (change.getCurrentState() == ConnectionState.CONNECTED) {
                    // Default to try to reconnect once connected
                    reconnect = true;
                }

                if (change.getCurrentState() == ConnectionState.DISCONNECTED) {
                    if (pusher != null) {
                        // Clean up connection listener since we're disconnected and if we connect again we'll create
                        // a new client.
                        pusher.getConnection().unbind(ConnectionState.ALL, this);
                    }

                    if (reconnect) {
                        // If reconnecting, this flag will get set to true once reconnected.
                        reconnect = false;
                        new Thread(() -> {
                            // Try the reconnect on a fresh thread.
                            LoggerFactory.getLogger(PusherManager.class).info("Attempting to reconnect Pusher.");
                            connect();
                        }).run();
                    }
                }
            }

            @Override
            public void onError(String message, String code, Exception e) {
                LoggerFactory.getLogger(PusherManager.class).error("Pusher connection error: \"" + message + "\"");
                if (reconnect) {
                    reconnect = false;
                    new Thread(() -> {
                        LoggerFactory.getLogger(PusherManager.class).info("Attempting to reconnect Pusher.");
                        connect();
                    }).run();
                }
            }
        };

        pusher.getConnection().bind(ConnectionState.ALL, connectionEventListener);

        channel.bind("requestAdded", pusherEvent -> {
            eventManager.dispatchEvent(gson.fromJson(pusherEvent.getData(), RequestAddedEvent.class));
        });
        channel.bind("requestLiked", pusherEvent -> {
            eventManager.dispatchEvent(gson.fromJson(pusherEvent.getData(), RequestLikedEvent.class));
        });
        channel.bind("requestListCleared", pusherEvent -> {
            eventManager.dispatchEvent(gson.fromJson(pusherEvent.getData(), RequestListClearedEvent.class));
        });
        channel.bind("requestPlayed", pusherEvent -> {
            eventManager.dispatchEvent(gson.fromJson(pusherEvent.getData(), RequestPlayedEvent.class));
        });
        channel.bind("requestRemoved", pusherEvent -> {
            eventManager.dispatchEvent(gson.fromJson(pusherEvent.getData(), RequestRemovedEvent.class));
        });
        channel.bind("requestUnliked", pusherEvent -> {
            eventManager.dispatchEvent(gson.fromJson(pusherEvent.getData(), RequestUnlikedEvent.class));
        });
        channel.bind("requestUpgraded", pusherEvent -> {
            eventManager.dispatchEvent(gson.fromJson(pusherEvent.getData(), RequestUpgradedEvent.class));
        });
        channel.bind("requestDowngraded", pusherEvent -> {
            eventManager.dispatchEvent(gson.fromJson(pusherEvent.getData(), RequestDowngradedEvent.class));
        });
        channel.bind("commandsUpdated", pusherEvent -> {
            eventManager.dispatchEvent(gson.fromJson(pusherEvent.getData(), CommandsUpdatedEvent.class));
        });
        channel.bind("eventStarted", pusherEvent -> {
            eventManager.dispatchEvent(gson.fromJson(pusherEvent.getData(), EventStartedEvent.class));
        });
        channel.bind("eventEntered", pusherEvent -> {
            eventManager.dispatchEvent(gson.fromJson(pusherEvent.getData(), EventEnteredEvent.class));
        });
        channel.bind("eventFinished", pusherEvent -> {
            eventManager.dispatchEvent(gson.fromJson(pusherEvent.getData(), EventFinishedEvent.class));
        });

        if (!configuration.enableIrc) {
            channel.bind("twitchCheer", pusherEvent -> {
                eventManager.dispatchEvent(gson.fromJson(pusherEvent.getData(), BitEvent.class));
            });
            channel.bind("twitchSubscribe", pusherEvent -> {
                eventManager.dispatchEvent(gson.fromJson(pusherEvent.getData(), SubscribeEvent.class));
            });
        }

        if (!configuration.isStreamlabsConnectable()) {
            channel.bind("streamlabsDonation", pusherEvent -> {
                eventManager.dispatchEvent(gson.fromJson(pusherEvent.getData(), NewDonationEvent.class));
            });
        }

        pusher.connect();
    }

    public void disconnect(boolean reconnect) {
        this.reconnect = reconnect;

        if (pusher == null || pusher.getConnection().getState() == ConnectionState.DISCONNECTED) {
            return;
        }

        pusher.unsubscribe(JAMISPHERE_EVENT);
        channel = null;

        pusher.disconnect();
    }

    public Pusher getPusher() {
        return pusher;
    }
}
