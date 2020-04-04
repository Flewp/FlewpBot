package com.flewp.flewpbot.model.jamisphere;

public class JamisphereGuessingGame extends JamisphereGame {
    public Integer answer;

    @Override
    public String toString() {
        return "JamisphereGuessingGame{" +
                "answer=" + answer +
                ", gameId='" + gameId + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", startsAt=" + startsAt +
                ", expiresAt=" + expiresAt +
                '}';
    }
}
