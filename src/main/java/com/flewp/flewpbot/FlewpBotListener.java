package com.flewp.flewpbot;

import com.flewp.flewpbot.model.events.jamisphere.*;
import com.flewp.flewpbot.model.events.twitch.*;

import javax.sound.midi.ShortMessage;

public interface FlewpBotListener {
    void onChatMessage(ChatEvent chatEvent);

    void onCheer(BitEvent bitEvent);

    void onSubscribe(SubscribeEvent subscribeEvent);

    void onNewDonation(NewDonationEvent newDonationEvent);

    void onRequestAdded(RequestAddedEvent requestAddedEvent);

    void onRequestLiked(RequestLikedEvent requestLikedEvent);

    void onRequestListCleared(RequestListClearedEvent requestListClearedEvent);

    void onRequestPlayed(RequestPlayedEvent requestPlayedEvent);

    void onRequestRemoved(RequestRemovedEvent requestRemovedEvent);

    void onRequestUnliked(RequestUnlikedEvent requestUnlikedEvent);

    void onRequestUpgraded(RequestUpgradedEvent requestUpgradedEvent);

    void onRequestDowngraded(RequestDowngradedEvent requestDowngradedEvent);

    void onCommandsUpdated(CommandsUpdatedEvent commandsUpdatedEvent);
}
