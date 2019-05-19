package com.flewp.flewpbot.app;

import com.flewp.flewpbot.FlewpBot;
import com.flewp.flewpbot.FlewpBotListener;
import com.flewp.flewpbot.FlewpBotListenerAdapter;
import com.flewp.flewpbot.event.*;

class FlewpBotConsole extends FlewpBotListenerAdapter {
    public static void main(String[] args) {
        FlewpBot flewpBot = new FlewpBot();
        flewpBot.addListener(new ConsoleListener());
        flewpBot.start();
    }

    private static class ConsoleListener implements FlewpBotListener {

        @Override
        public void onWhisperMessage(WhisperEvent whisperEvent) {
            System.out.println(whisperEvent.toString());
        }

        @Override
        public void onChatMessage(ChatEvent chatEvent) {
            System.out.println(chatEvent.toString());
        }

        @Override
        public void onCheer(BitEvent bitEvent) {
            System.out.println(bitEvent.toString());
        }

        @Override
        public void onSubscribe(SubscribeEvent subscribeEvent) {
            System.out.println(subscribeEvent.toString());
        }

        @Override
        public void onNewDonation(NewDonationEvent newDonationEvent) {
            System.out.println(newDonationEvent.toString());
        }
    }
}
