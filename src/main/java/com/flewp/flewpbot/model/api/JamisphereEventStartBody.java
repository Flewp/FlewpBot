package com.flewp.flewpbot.model.api;

import java.util.ArrayList;
import java.util.List;

public class JamisphereEventStartBody {
    public String type;

    public String description;
    public String inputLabel;
    public Integer length;

    public String requestId;

    public String choiceType;
    public List<String> choices;

    public String rewardName;
    public String rewardUserInput;

    public static JamisphereEventStartBody startGuessingGame(String requestId, Integer length) {
        JamisphereEventStartBody jamisphereEventStartBody = new JamisphereEventStartBody();

        jamisphereEventStartBody.type = "guessingGame";
        jamisphereEventStartBody.requestId = requestId;
        jamisphereEventStartBody.length = length;

        return jamisphereEventStartBody;
    }

    public static JamisphereEventStartBody startChoiceGame(boolean isSongChoices, List<String> choices, Integer length) {
        JamisphereEventStartBody jamisphereEventStartBody = new JamisphereEventStartBody();

        jamisphereEventStartBody.type = "multipleChoice";
        jamisphereEventStartBody.choiceType = isSongChoices ? "song" : "other";
        jamisphereEventStartBody.length = length;
        jamisphereEventStartBody.choices = new ArrayList<>(choices);

        return jamisphereEventStartBody;
    }

    public static JamisphereEventStartBody startClaimGame(String rewardName, String rewardUserInput) {
        JamisphereEventStartBody jamisphereEventStartBody = new JamisphereEventStartBody();

        jamisphereEventStartBody.type = "claim";
        jamisphereEventStartBody.rewardName = rewardName;
        jamisphereEventStartBody.rewardUserInput = rewardUserInput;

        return jamisphereEventStartBody;
    }
}
