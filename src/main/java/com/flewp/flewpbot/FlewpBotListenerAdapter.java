package com.flewp.flewpbot;

import com.flewp.flewpbot.event.NewDonationEvent;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.chat.events.channel.CheerEvent;
import com.github.twitch4j.chat.events.channel.SubscriptionEvent;
import com.github.twitch4j.common.events.user.PrivateMessageEvent;

public class FlewpBotListenerAdapter implements FlewpBotListener {
    @Override
    public void onPrivateMessage(PrivateMessageEvent privateMessageEvent) {

    }

    @Override
    public void onChatMessage(ChannelMessageEvent channelMessageEvent) {

    }

    @Override
    public void onCheer(CheerEvent cheerEvent) {

    }

    @Override
    public void onSubscription(SubscriptionEvent subscriptionEvent) {

    }

    @Override
    public void onNewDonation(NewDonationEvent newDonationEvent) {

    }
}
