package com.flewp.flewpbot.model.jamisphere;

public class JamisphereUser {
    public String twitchUserName;
    public String twitchUserId;
    public Integer awesomeness;
    public Integer guessingGamesWon;

    @Override
    public String toString() {
        return "JamisphereUser{" +
                "twitchUserName='" + twitchUserName + '\'' +
                ", twitchUserId='" + twitchUserId + '\'' +
                ", awesomeness=" + awesomeness +
                ", guessingGamesWon=" + guessingGamesWon +
                '}';
    }
}
