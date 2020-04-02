package com.flewp.flewpbot.model.events.jamisphere;

import com.flewp.flewpbot.model.events.BaseEvent;
import com.flewp.flewpbot.model.jamisphere.JamisphereGuess;
import com.flewp.flewpbot.model.jamisphere.JamisphereGuessingGame;
import com.flewp.flewpbot.model.jamisphere.JamisphereRequest;
import com.flewp.flewpbot.model.jamisphere.JamisphereUser;

public class GuessingGameAnsweredEvent extends BaseEvent {
    public JamisphereUser winner;
    public JamisphereRequest request;
    public JamisphereGuess guess;
    public JamisphereGuessingGame guessingGame;


}
