package com.flewp.flewpbot.model.api;

public class JamisphereGuessingGameGuessBehalfBody {
    public int guess;
    public String behalfOfUserName;

    public JamisphereGuessingGameGuessBehalfBody(int guess, String behalfOfUserName) {
        this.guess = guess;
        this.behalfOfUserName = behalfOfUserName;
    }
}
