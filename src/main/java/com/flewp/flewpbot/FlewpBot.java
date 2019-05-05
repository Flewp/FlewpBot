package com.flewp.flewpbot;

import com.flewp.flewpbot.api.StreamlabsAPIController;
import com.flewp.flewpbot.api.TwitchAPIController;
import com.flewp.flewpbot.api.TwitchTokenGeneratorAPI;
import com.flewp.flewpbot.event.NewDonationEvent;
import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.chat.events.channel.CheerEvent;
import com.github.twitch4j.chat.events.channel.GiftSubscriptionsEvent;
import com.github.twitch4j.chat.events.channel.SubscriptionEvent;
import com.github.twitch4j.common.events.user.PrivateMessageEvent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class FlewpBot {
    private TwitchAPIController twitchAPIController;
    private StreamlabsAPIController streamlabsAPIController;

    private List<FlewpBotListener> listenerList = new ArrayList<>();

    public FlewpBot() {
        Configuration configuration = Configuration.getInstance();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://twitchtokengenerator.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TwitchTokenGeneratorAPI twitchTokenGeneratorAPI = retrofit.create(TwitchTokenGeneratorAPI.class);

        EventManager eventManager = new EventManager();
        eventManager.onEvent(PrivateMessageEvent.class).subscribe(this::onPrivateMessage);
        eventManager.onEvent(ChannelMessageEvent.class).subscribe(this::onChatMessage);
        eventManager.onEvent(CheerEvent.class).subscribe(this::onCheer);
        eventManager.onEvent(SubscriptionEvent.class).subscribe(this::onSubscription);
        eventManager.onEvent(GiftSubscriptionsEvent.class).subscribe(this::onGiftSubscription);
        eventManager.onEvent(NewDonationEvent.class).subscribe(this::onNewDonation);

        twitchAPIController = new TwitchAPIController(configuration, twitchTokenGeneratorAPI, eventManager);
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
        listenerList.forEach(listener -> listener.onNewDonation(newDonationEvent));
    }

    synchronized private void onGiftSubscription(GiftSubscriptionsEvent giftSubscriptionsEvent) {
        listenerList.forEach(listener -> listener.onGiftSubscription(giftSubscriptionsEvent));
    }

    synchronized private void onSubscription(SubscriptionEvent subscriptionEvent) {
        listenerList.forEach(listener -> listener.onSubscription(subscriptionEvent));
    }

    synchronized private void onCheer(CheerEvent cheerEvent) {
        listenerList.forEach(listener -> listener.onCheer(cheerEvent));
    }

    synchronized private void onChatMessage(ChannelMessageEvent channelMessageEvent) {
        listenerList.forEach(listener -> listener.onChatMessage(channelMessageEvent));
    }

    synchronized private void onPrivateMessage(PrivateMessageEvent privateMessageEvent) {
        listenerList.forEach(listener -> listener.onPrivateMessage(privateMessageEvent));
    }
}
