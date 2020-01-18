package com.flewp.flewpbot.model.jamisphere;

import java.util.List;

public class JamisphereChoiceGame extends JamisphereGame {
    // type = "poll, vote, quiz"
    public String choiceType; // "request, song, none"
    public List<String> choices;
    public List<String> resolvedChoices;
    public String answer;

    @Override
    public String toString() {
        return "JamisphereChoiceGame{" +
                "choiceType='" + choiceType + '\'' +
                ", choices=" + choices +
                ", resolvedChoices=" + resolvedChoices +
                ", answer='" + answer + '\'' +
                ", gameId='" + gameId + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", startsAt=" + startsAt +
                ", expiresAt=" + expiresAt +
                '}';
    }
}
