package com.flewp.flewpbot;

import com.flewp.flewpbot.event.*;

public interface FlewpBotListener {
    void onWhisperMessage(WhisperEvent whisperEvent);

    void onChatMessage(ChatEvent chatEvent);

    void onCheer(BitEvent bitEvent);

    void onSubscribe(SubscribeEvent subscribeEvent);

    void onNewDonation(NewDonationEvent newDonationEvent);
}
