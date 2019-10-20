package com.flewp.flewpbot.model.api;

public class JamisphereGuessingGameGuessBehalfBody {
    public int guess;
    public String behalfOfUserName;
    public String guessingGameId;

    public JamisphereGuessingGameGuessBehalfBody(int guess, String behalfOfUserName, String guessingGameId) {
        this.guess = guess;
        this.behalfOfUserName = behalfOfUserName;
        this.guessingGameId = guessingGameId;
    }
}
