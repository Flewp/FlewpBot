package com.flewp.flewpbot.model.events.jamisphere;

import com.flewp.flewpbot.model.jamisphere.JamisphereCommand;
import com.github.philippheuer.events4j.domain.Event;

import java.util.List;

public class CommandsUpdatedEvent extends Event {
    public List<JamisphereCommand> commands;
    public String operation;

    @Override
    public String toString() {
        return "CommandsUpdatedEvent{}";
    }
}
