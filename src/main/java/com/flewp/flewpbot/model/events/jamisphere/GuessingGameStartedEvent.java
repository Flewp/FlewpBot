package com.flewp.flewpbot.model.events.jamisphere;

import com.flewp.flewpbot.model.jamisphere.JamisphereGuessingGame;
import com.github.philippheuer.events4j.domain.Event;

public class GuessingGameStartedEvent extends Event {
    public JamisphereGuessingGame guessingGame;

    @Override
    public String toString() {
        return "GuessingGameStartedEvent{" +
                "guessingGame=" + guessingGame +
                '}';
    }
}
