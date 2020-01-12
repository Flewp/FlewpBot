package com.flewp.flewpbot.pusher;

import com.flewp.flewpbot.Configuration;
import com.flewp.flewpbot.model.events.jamisphere.*;
import com.flewp.flewpbot.model.events.twitch.BitEvent;
import com.flewp.flewpbot.model.events.twitch.NewDonationEvent;
import com.flewp.flewpbot.model.events.twitch.SubscribeEvent;
import com.github.philippheuer.events4j.EventManager;
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

    @Inject
    public PusherManager(Configuration configuration, EventManager eventManager) {
        this.configuration = configuration;
        this.eventManager = eventManager;

        if (configuration.pusherCluster == null || configuration.pusherKey == null) {
            LoggerFactory.getLogger(PusherManager.class).info("Pusher credentials not provided, Pusher will not be connected.");
            return;
        }

        PusherOptions options = new PusherOptions().setCluster(configuration.pusherCluster);
        pusher = new Pusher(configuration.pusherKey, options);
    }

    public void connect() {
        if (pusher == null || channel != null) {
            return;
        }

        channel = pusher.subscribe(JAMISPHERE_EVENT);
        pusher.getConnection().bind(ConnectionState.ALL, new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                LoggerFactory.getLogger(PusherManager.class).info("Pusher connection changed from \""
                        + change.getPreviousState().name() + "\" to \"" + change.getCurrentState().name() + "\"");
            }

            @Override
            public void onError(String message, String code, Exception e) {
                LoggerFactory.getLogger(PusherManager.class).error("Pusher connection error: \"" + message + "\"");
            }
        });

        channel.bind("guessingGameAnswered", pusherEvent -> {
            eventManager.dispatchEvent(gson.fromJson(pusherEvent.getData(), GuessingGameAnsweredEvent.class));
        });
        channel.bind("guessingGameStart", pusherEvent -> {
            eventManager.dispatchEvent(gson.fromJson(pusherEvent.getData(), GuessingGameStartedEvent.class));
        });
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
        channel.bind("choiceGameStarted", pusherEvent -> {
            eventManager.dispatchEvent(gson.fromJson(pusherEvent.getData(), ChoiceGameStartedEvent.class));
        });
        channel.bind("choiceEntered", pusherEvent -> {
            eventManager.dispatchEvent(gson.fromJson(pusherEvent.getData(), ChoiceGameChoiceEnteredEvent.class));
        });
        channel.bind("choiceGameAnswered", pusherEvent -> {
            eventManager.dispatchEvent(gson.fromJson(pusherEvent.getData(), ChoiceGameAnsweredEvent.class));
        });
        channel.bind("commandsUpdated", pusherEvent -> {
            eventManager.dispatchEvent(gson.fromJson(pusherEvent.getData(), CommandsUpdatedEvent.class));
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
}
