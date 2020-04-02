package com.flewp.flewpbot.model.events.jamisphere;

import com.flewp.flewpbot.model.events.BaseEvent;
import com.flewp.flewpbot.model.jamisphere.JamisphereChoiceGame;

public class ChoiceGameStartedEvent extends BaseEvent {
    public JamisphereChoiceGame choiceGame;

    @Override
    public String toString() {
        return "ChoiceGameStartedEvent{" +
                "choiceGame=" + choiceGame +
                '}';
    }
}
