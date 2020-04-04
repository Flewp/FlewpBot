package com.flewp.flewpbot.model.api;

public class JamisphereGuessingGameStartBody {
    public String requestId;
    public Integer length;

    public JamisphereGuessingGameStartBody(String requestId, Integer length) {
        this.requestId = requestId;
        this.length = length;
    }
}