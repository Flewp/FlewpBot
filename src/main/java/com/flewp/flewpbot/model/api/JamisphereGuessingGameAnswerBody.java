package com.flewp.flewpbot.model.api;

public class JamisphereGuessingGameAnswerBody {
    public int answer;
    public String guessingGameId;

    public JamisphereGuessingGameAnswerBody(int answer, String guessingGameId) {
        this.answer = answer;
        this.guessingGameId = guessingGameId;
    }
}
