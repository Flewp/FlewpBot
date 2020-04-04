package com.flewp.flewpbot.model.api;

import com.flewp.flewpbot.model.jamisphere.JamisphereGuess;
import com.flewp.flewpbot.model.jamisphere.JamisphereGuessingGame;
import com.flewp.flewpbot.model.jamisphere.JamisphereRequest;
import com.flewp.flewpbot.model.jamisphere.JamisphereUser;

public class JamisphereGuessingGameAnswerResponse extends JamisphereBaseResponse {
    public JamisphereUser winner;
    public JamisphereRequest request;
    public JamisphereGuess guess;
    public JamisphereGuessingGame guessingGame;
}
