package com.flewp.flewpbot.app;

import com.flewp.flewpbot.FlewpBot;
import com.flewp.flewpbot.FlewpBotListener;
import com.flewp.flewpbot.FlewpBotListenerAdapter;
import com.flewp.flewpbot.event.*;

class FlewpBotConsole extends FlewpBotListenerAdapter {
    public static void main(String[] args) {
        FlewpBot flewpBot = new FlewpBot();
        flewpBot.addListener(new ConsoleListener());
        flewpBot.startChatBot();
        flewpBot.startQueryingDonations();
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
    }
}
