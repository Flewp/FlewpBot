package com.flewp.flewpbot.model.jamisphere;

public class JamisphereGame {
    public String gameId;
    public String type;
    public String description;
    public Long startsAt;
    public Long expiresAt;

    @Override
    public String toString() {
        return "JamisphereGame{" +
                "gameId='" + gameId + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", startsAt=" + startsAt +
                ", expiresAt=" + expiresAt +
                '}';
    }
}
