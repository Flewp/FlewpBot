package com.flewp.flewpbot;

import com.flewp.flewpbot.event.NewDonationEvent;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.chat.events.channel.CheerEvent;
import com.github.twitch4j.chat.events.channel.GiftSubscriptionsEvent;
import com.github.twitch4j.chat.events.channel.SubscriptionEvent;
import com.github.twitch4j.common.events.user.PrivateMessageEvent;

public interface FlewpBotListener {
    void onPrivateMessage(PrivateMessageEvent privateMessageEvent);

    void onChatMessage(ChannelMessageEvent channelMessageEvent);

    void onCheer(CheerEvent cheerEvent);

    void onSubscription(SubscriptionEvent subscriptionEvent);

    void onGiftSubscription(GiftSubscriptionsEvent giftSubscriptionsEvent);

    void onNewDonation(NewDonationEvent newDonationEvent);
}
