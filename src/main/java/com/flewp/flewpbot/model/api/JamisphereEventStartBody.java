package com.flewp.flewpbot.model.api;

import java.util.List;

public class JamisphereEventStartBody {
    public String type;

    public String description;
    public String inputLabel;
    public Integer length;

    public String requestId;

    public String choiceType;
    public List<String> choices;

    String rewardName;
    String rewardPrompt;
    String rewardUserInput;
    Integer rewardCost;
}
