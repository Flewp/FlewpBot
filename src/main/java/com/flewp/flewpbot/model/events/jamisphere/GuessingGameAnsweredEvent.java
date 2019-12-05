package com.flewp.flewpbot.model.events.jamisphere;

import com.flewp.flewpbot.model.jamisphere.JamisphereGuess;
import com.flewp.flewpbot.model.jamisphere.JamisphereGuessingGame;
import com.flewp.flewpbot.model.jamisphere.JamisphereRequest;
import com.flewp.flewpbot.model.jamisphere.JamisphereUser;
import com.github.philippheuer.events4j.domain.Event;

public class GuessingGameAnsweredEvent extends Event {
    public JamisphereUser winner;
    public JamisphereRequest request;
    public JamisphereGuess guess;
    public JamisphereGuessingGame guessingGame;
}
