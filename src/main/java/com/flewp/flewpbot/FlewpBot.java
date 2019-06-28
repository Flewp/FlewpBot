package com.flewp.flewpbot;

import com.flewp.flewpbot.api.StreamlabsAPI;
import com.flewp.flewpbot.api.TwitchHelixAPI;
import com.flewp.flewpbot.api.TwitchKrakenAPI;
import com.flewp.flewpbot.api.controller.StreamlabsAPIController;
import com.flewp.flewpbot.api.controller.TwitchAPIController;
import com.flewp.flewpbot.event.*;
import com.flewp.flewpbot.model.kraken.KrakenChatRoom;
import com.github.philippheuer.events4j.EventManager;
import org.pircbotx.PircBotX;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class FlewpBot {

    private List<FlewpBotListener> listenerList = new ArrayList<>();

    private Configuration configuration;

    @Inject
    EventManager eventManager;

    @Inject
    TwitchHelixAPI twitchHelixAPI;

    @Inject
    TwitchKrakenAPI twitchKrakenAPI;

    @Inject
    StreamlabsAPI streamlabsAPI;

    @Inject
    TwitchAPIController twitchAPIController;

    @Inject
    StreamlabsAPIController streamlabsAPIController;


    public FlewpBot() {
        configuration = Configuration.getInstance();

        DaggerFlewpBotComponent.builder()
                .flewpBotModule(new FlewpBotModule(configuration))
                .build().inject(this);

        eventManager.onEvent(WhisperEvent.class).subscribe(this::onWhisperMessage);
        eventManager.onEvent(ChatEvent.class).subscribe(this::onChatMessage);
        eventManager.onEvent(BitEvent.class).subscribe(this::onCheer);
        eventManager.onEvent(SubscribeEvent.class).subscribe(this::onSubscribe);
        eventManager.onEvent(NewDonationEvent.class).subscribe(this::onNewDonation);
    }

    synchronized public void start() {
        twitchAPIController.startChatBot();
        streamlabsAPIController.startQueryingDonations();
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

    public TwitchHelixAPI getTwitchHelixAPI() {
        return twitchHelixAPI;
    }

    public TwitchKrakenAPI getTwitchKrakenAPI() {
        return twitchKrakenAPI;
    }

    public StreamlabsAPI getStreamlabsAPI() {
        return streamlabsAPI;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public List<KrakenChatRoom> getConnectedChatRooms() {
        return twitchAPIController.getChatRoomList();
    }

    public String getStreamerUserId() {
        return twitchAPIController.getStreamerUserId();
    }

    public String getStreamerChannelId() {
        return twitchAPIController.getStreamerChannelId();
    }
}
