package com.flewp.flewpbot.model.api;

import java.util.List;

public class JamisphereChoiceGameStartBody {
    public String description;
    public String type;
    public String choiceType;
    public List<String> choices;
    public Integer length;

    public JamisphereChoiceGameStartBody(String description, String type, String choiceType, List<String> choices, Integer length) {
        this.description = description;
        this.type = type;
        this.choiceType = choiceType;
        this.choices = choices;
        this.length = length;
    }
}
