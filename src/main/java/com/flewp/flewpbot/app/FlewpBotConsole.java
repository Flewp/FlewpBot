package com.flewp.flewpbot.app;

import com.flewp.flewpbot.FlewpBot;
import com.flewp.flewpbot.FlewpBotListener;
import com.flewp.flewpbot.FlewpBotListenerAdapter;
import com.flewp.flewpbot.model.events.jamisphere.*;
import com.flewp.flewpbot.model.events.twitch.*;

class FlewpBotConsole extends FlewpBotListenerAdapter {
    static FlewpBot flewpBot;
    public static void main(String[] args) {
        flewpBot = new FlewpBot();
        flewpBot.addListener(new ConsoleListener());
        flewpBot.start();
    }

    private static class ConsoleListener implements FlewpBotListener {

        @Override
        public void onWhisperMessage(WhisperEvent whisperEvent) {

        }

        @Override
        public void onChatMessage(ChatEvent chatEvent) {

        }

        @Override
        public void onCheer(BitEvent bitEvent) {

        }

        @Override
        public void onSubscribe(SubscribeEvent subscribeEvent) {

        }

        @Override
        public void onNewDonation(NewDonationEvent newDonationEvent) {

        }

        @Override
        public void onGuessingGameAnswered(GuessingGameAnsweredEvent guessingGameAnsweredEvent) {

        }

        @Override
        public void onGuessingGameStarted(GuessingGameStartedEvent guessingGameStartedEvent) {

        }

        @Override
        public void onRequestAdded(RequestAddedEvent requestAddedEvent) {

        }

        @Override
        public void onRequestLiked(RequestLikedEvent requestLikedEvent) {

        }

        @Override
        public void onRequestListCleared(RequestListClearedEvent requestListClearedEvent) {

        }

        @Override
        public void onRequestPlayed(RequestPlayedEvent requestPlayedEvent) {

        }

        @Override
        public void onRequestRemoved(RequestRemovedEvent requestRemovedEvent) {

        }

        @Override
        public void onRequestUnliked(RequestUnlikedEvent requestUnlikedEvent) {

        }

        @Override
        public void onRequestUpgraded(RequestUpgradedEvent requestUpgradedEvent) {

        }
    }
}
