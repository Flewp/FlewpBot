package com.flewp.flewpbot.app;

import com.flewp.flewpbot.FlewpBot;
import com.flewp.flewpbot.FlewpBotListener;
import com.flewp.flewpbot.FlewpBotListenerAdapter;
import com.flewp.flewpbot.event.NewDonationEvent;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.chat.events.channel.CheerEvent;
import com.github.twitch4j.chat.events.channel.GiftSubscriptionsEvent;
import com.github.twitch4j.chat.events.channel.SubscriptionEvent;
import com.github.twitch4j.common.events.user.PrivateMessageEvent;

class FlewpBotConsole extends FlewpBotListenerAdapter {
    public static void main(String[] args) {
        FlewpBot flewpBot = new FlewpBot();
        flewpBot.addListener(new ConsoleListener());
        flewpBot.start();
    }

    private static class ConsoleListener implements FlewpBotListener {
        @Override
        public void onPrivateMessage(PrivateMessageEvent privateMessageEvent) {
            System.out.println(privateMessageEvent.toString());
        }

        @Override
        public void onChatMessage(ChannelMessageEvent channelMessageEvent) {
            System.out.println(channelMessageEvent.toString());
        }

        @Override
        public void onCheer(CheerEvent cheerEvent) {
            System.out.println(cheerEvent.toString());
        }

        @Override
        public void onSubscription(SubscriptionEvent subscriptionEvent) {
            System.out.println(subscriptionEvent.toString());
        }

        @Override
        public void onGiftSubscription(GiftSubscriptionsEvent giftSubscriptionsEvent) {
            System.out.println(giftSubscriptionsEvent.toString());
        }

        @Override
        public void onNewDonation(NewDonationEvent newDonationEvent) {
            System.out.println(newDonationEvent.toString());
        }
    }
}
