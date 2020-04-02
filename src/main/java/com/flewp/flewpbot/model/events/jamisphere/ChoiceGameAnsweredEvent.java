package com.flewp.flewpbot.model.events.jamisphere;

import com.flewp.flewpbot.model.events.BaseEvent;
import com.flewp.flewpbot.model.jamisphere.JamisphereChoiceGame;

public class ChoiceGameAnsweredEvent extends BaseEvent {
    public JamisphereChoiceGame choiceGame;

    @Override
    public String toString() {
        return "ChoiceGameAnsweredEvent{" +
                "choiceGame=" + choiceGame +
                '}';
    }
}
