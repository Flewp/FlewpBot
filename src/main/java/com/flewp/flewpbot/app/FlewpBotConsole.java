package com.flewp.flewpbot.app;

import com.flewp.flewpbot.FlewpBot;
import com.flewp.flewpbot.FlewpBotListener;
import com.flewp.flewpbot.FlewpBotListenerAdapter;
import com.flewp.flewpbot.FlewpBotMIDIReceiver;
import com.flewp.flewpbot.model.events.jamisphere.*;
import com.flewp.flewpbot.model.events.twitch.*;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

class FlewpBotConsole extends FlewpBotListenerAdapter {
    static FlewpBot flewpBot;

    public static void main(String[] args) {
        flewpBot = new FlewpBot();
        flewpBot.addListener(new ConsoleListener());
        flewpBot.addMIDIReceiver(new FlewpBotMIDIReceiver((deviceName, midiMessage, timestamp) -> {
            int i = 0;
        }, "td-30", false));
        flewpBot.start();
        flewpBot.sendMIDIMessage("3- TD-30WOW", new ShortMessage(), 0L);
    }

    private static class ConsoleListener implements FlewpBotListener {
        @Override
        public void onChatMessage(ChatEvent chatEvent) {
            flewpBot.sendTwitchChatMessage("wow");
//            flewpBot.sendTwitchChatMessage(flewpBot.getTwitchUserPermission(chatEvent.getEventUser()));
        }

        @Override
        public void onCheer(BitEvent bitEvent) {
            int i = 0;
        }

        @Override
        public void onSubscribe(SubscribeEvent subscribeEvent) {
            int i = 0;
        }

        @Override
        public void onNewDonation(NewDonationEvent newDonationEvent) {
            int i = 0;
        }

        @Override
        public void onGuessingGameAnswered(GuessingGameAnsweredEvent guessingGameAnsweredEvent) {
            int i = 0;
        }

        @Override
        public void onGuessingGameStarted(GuessingGameStartedEvent guessingGameStartedEvent) {
            int i = 0;
        }

        @Override
        public void onRequestAdded(RequestAddedEvent requestAddedEvent) {
            int i = 0;
        }

        @Override
        public void onRequestLiked(RequestLikedEvent requestLikedEvent) {
            int i = 0;
        }

        @Override
        public void onRequestListCleared(RequestListClearedEvent requestListClearedEvent) {
            int i = 0;
        }

        @Override
        public void onRequestPlayed(RequestPlayedEvent requestPlayedEvent) {
            int i = 0;
        }

        @Override
        public void onRequestRemoved(RequestRemovedEvent requestRemovedEvent) {
            int i = 0;
        }

        @Override
        public void onRequestUnliked(RequestUnlikedEvent requestUnlikedEvent) {
            int i = 0;
        }

        @Override
        public void onRequestUpgraded(RequestUpgradedEvent requestUpgradedEvent) {
            int i = 0;
        }

        @Override
        public void onRequestDowngraded(RequestDowngradedEvent requestDowngradedEvent) {
            int i = 0;
        }

        @Override
        public void onChoiceGameStarted(ChoiceGameStartedEvent choiceGameStartedEvent) {
            int i = 0;
        }

        @Override
        public void onChoiceGameAnswered(ChoiceGameAnsweredEvent choiceGameAnsweredEvent) {
            int i = 0;
        }

        @Override
        public void onChoiceGameChoiceEntered(ChoiceGameChoiceEnteredEvent choiceGameChoiceEnteredEvent) {
            int i = 0;
        }

        @Override
        public void onCommandsUpdated(CommandsUpdatedEvent commandsUpdatedEvent) {
            int i = 0;
        }
    }
}
