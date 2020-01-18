package com.flewp.flewpbot.model.events.jamisphere;

import com.github.philippheuer.events4j.domain.Event;

public class ChoiceGameChoiceEnteredEvent extends Event {
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
