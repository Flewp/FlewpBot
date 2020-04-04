package com.flewp.flewpbot.model.events.jamisphere;

import com.flewp.flewpbot.model.events.BaseEvent;

public class ChoiceGameChoiceEnteredEvent extends BaseEvent {
    public String choice;
    public String choiceGameId;

    @Override
    public String toString() {
        return "ChoiceGameChoiceEnteredEvent{" +
                "choice='" + choice + '\'' +
                ", choiceGameId='" + choiceGameId + '\'' +
                '}';
    }
}
