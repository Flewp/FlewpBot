package com.flewp.flewpbot.model.jamisphere;

import java.util.List;

public class JamisphereChoiceGame {
    public String gameId;
    public String type; // "poll, vote, quiz"
    public String choiceType; // "request, song, none"
    public String description;
    public List<String> choices;
    public Long startsAt;
    public Long expiresAt;
    public String answer;
}
