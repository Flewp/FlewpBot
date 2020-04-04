package com.flewp.flewpbot.model.events.jamisphere;

import com.flewp.flewpbot.model.events.BaseEvent;
import com.flewp.flewpbot.model.jamisphere.JamisphereGuessingGame;

public class GuessingGameStartedEvent extends BaseEvent {
    public JamisphereGuessingGame guessingGame;

    @Override
    public String toString() {
        return "GuessingGameStartedEvent{" +
                "guessingGame=" + guessingGame +
                '}';
    }
}
