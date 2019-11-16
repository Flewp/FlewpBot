package com.flewp.flewpbot.pusher;

import com.flewp.flewpbot.Configuration;
import com.flewp.flewpbot.model.events.jamisphere.*;
import com.github.philippheuer.events4j.EventManager;
import com.google.gson.Gson;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;

import javax.inject.Inject;

public class PusherManager {
    private static final String JAMISPHERE_EVENT = "jamisphere";
    private Gson gson = new Gson();
    private Pusher pusher;
    private EventManager eventManager;

    private Channel channel;

    @Inject
    public PusherManager(Configuration configuration, EventManager eventManager) {
        this.eventManager = eventManager;

        PusherOptions options = new PusherOptions().setCluster(configuration.pusherCluster);
        pusher = new Pusher(configuration.pusherKey, options);
    }

    public void connect() {
        if (channel != null) {
            return;
        }

        channel = pusher.subscribe(JAMISPHERE_EVENT);
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

        pusher.connect();
    }
}
