package com.flewp.flewpbot.model.jamisphere;

import java.util.List;

public class JamisphereChoiceGame extends JamisphereGame {
    // type = "poll, vote, quiz"
    public String choiceType; // "request, song, none"
    public List<String> choices;
    public List<String> resolvedChoices;
    public String answer;
}
