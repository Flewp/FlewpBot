package com.flewp.flewpbot.model.events.jamisphere;

import com.flewp.flewpbot.model.jamisphere.JamisphereChoiceGame;
import com.github.philippheuer.events4j.domain.Event;

public class ChoiceGameStartedEvent extends Event {
    public JamisphereChoiceGame choiceGame;
}
