package com.flewp.flewpbot;

import com.flewp.flewpbot.model.events.jamisphere.*;
import com.flewp.flewpbot.model.events.twitch.*;

import javax.sound.midi.ShortMessage;

public interface FlewpBotListener {
    void onWhisperMessage(WhisperEvent whisperEvent);

    void onChatMessage(ChatEvent chatEvent);

    void onCheer(BitEvent bitEvent);

    void onSubscribe(SubscribeEvent subscribeEvent);

    void onNewDonation(NewDonationEvent newDonationEvent);

    void onGuessingGameAnswered(GuessingGameAnsweredEvent guessingGameAnsweredEvent);

    void onGuessingGameStarted(GuessingGameStartedEvent guessingGameStartedEvent);

    void onRequestAdded(RequestAddedEvent requestAddedEvent);

    void onRequestLiked(RequestLikedEvent requestLikedEvent);

    void onRequestListCleared(RequestListClearedEvent requestListClearedEvent);

    void onRequestPlayed(RequestPlayedEvent requestPlayedEvent);

    void onRequestRemoved(RequestRemovedEvent requestRemovedEvent);

    void onRequestUnliked(RequestUnlikedEvent requestUnlikedEvent);

    void onRequestUpgraded(RequestUpgradedEvent requestUpgradedEvent);

    void onRequestDowngraded(RequestDowngradedEvent requestDowngradedEvent);

    void onChoiceGameStarted(ChoiceGameStartedEvent choiceGameStartedEvent);

    void onChoiceGameAnswered(ChoiceGameAnsweredEvent choiceGameAnsweredEvent);

    void onChoiceGameChoiceEntered(ChoiceGameChoiceEnteredEvent choiceGameChoiceEnteredEvent);

    void onCommandsUpdated(CommandsUpdatedEvent commandsUpdatedEvent);

    void onMidiMessage(String deviceName, ShortMessage message, int timestamp);
}
