package com.flewp.flewpbot;

import com.flewp.flewpbot.api.StreamlabsAPIController;
import com.flewp.flewpbot.api.TwitchAPIController;
import com.flewp.flewpbot.api.TwitchKrakenAPI;
import com.flewp.flewpbot.event.*;
import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.streamlabs4j.StreamlabsClient;
import org.pircbotx.PircBotX;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class FlewpBot {
    private TwitchAPIController twitchAPIController;
    private StreamlabsAPIController streamlabsAPIController;

    private List<FlewpBotListener> listenerList = new ArrayList<>();

    private Configuration configuration;

    public FlewpBot() {
        configuration = Configuration.getInstance();

        EventManager eventManager = new EventManager();
        eventManager.onEvent(WhisperEvent.class).subscribe(this::onWhisperMessage);
        eventManager.onEvent(ChatEvent.class).subscribe(this::onChatMessage);
        eventManager.onEvent(BitEvent.class).subscribe(this::onCheer);
        eventManager.onEvent(SubscribeEvent.class).subscribe(this::onSubscribe);
        eventManager.onEvent(NewDonationEvent.class).subscribe(this::onNewDonation);

        twitchAPIController = new TwitchAPIController(configuration, eventManager);
        streamlabsAPIController = new StreamlabsAPIController(configuration, eventManager);
    }

    synchronized public void start() {
        twitchAPIController.connect();
        streamlabsAPIController.connect();
    }

    synchronized public void addListener(FlewpBotListener listener) {
        if (!listenerList.contains(listener)) {
            listenerList.add(listener);
        }
    }

    synchronized public void removeListener(FlewpBotListener listener) {
        listenerList.remove(listener);
    }

    synchronized private void onNewDonation(NewDonationEvent newDonationEvent) {
        LoggerFactory.getLogger(FlewpBot.class).info(newDonationEvent.toString());
        listenerList.forEach(listener -> listener.onNewDonation(newDonationEvent));
    }

    synchronized private void onSubscribe(SubscribeEvent subscribeEvent) {
        LoggerFactory.getLogger(FlewpBot.class).info(subscribeEvent.toString());
        listenerList.forEach(listener -> listener.onSubscribe(subscribeEvent));
    }

    synchronized private void onCheer(BitEvent bitEvent) {
        LoggerFactory.getLogger(FlewpBot.class).info(bitEvent.toString());
        listenerList.forEach(listener -> listener.onCheer(bitEvent));
    }

    synchronized private void onChatMessage(ChatEvent chatEvent) {
        LoggerFactory.getLogger(FlewpBot.class).info(chatEvent.toString());
        listenerList.forEach(listener -> listener.onChatMessage(chatEvent));
    }

    synchronized private void onWhisperMessage(WhisperEvent whisperEvent) {
        LoggerFactory.getLogger(FlewpBot.class).info(whisperEvent.toString());
        listenerList.forEach(listener -> listener.onWhisperMessage(whisperEvent));
    }

    public PircBotX getPircBotX() {
        return twitchAPIController.getPircBotX();
    }

    public TwitchClient getTwitchClient() {
        return twitchAPIController.getTwitchClient();
    }

    public TwitchKrakenAPI getTwitchKrakenAPI() {
        return twitchAPIController.getTwitchKrakenAPI();
    }

    public StreamlabsClient getStreamlabsClient() {
        return streamlabsAPIController.getStreamlabsClient();
    }
    
    public Configuration getConfiguration() {
        return configuration;
    }
}
