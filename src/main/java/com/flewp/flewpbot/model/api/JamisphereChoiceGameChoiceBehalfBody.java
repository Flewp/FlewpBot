package com.flewp.flewpbot.model.api;

public class JamisphereChoiceGameChoiceBehalfBody {
    public String choiceGameId;
    public String choice;
    public String behalfOfUserName;

    public JamisphereChoiceGameChoiceBehalfBody(String choiceGameId, String choice, String behalfOfUserName) {
        this.choiceGameId = choiceGameId;
        this.choice = choice;
        this.behalfOfUserName = behalfOfUserName;
    }
}
