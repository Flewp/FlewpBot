package com.flewp.flewpbot.model.events.jamisphere;

import com.flewp.flewpbot.model.events.BaseEvent;
import com.flewp.flewpbot.model.jamisphere.JamisphereCommand;

import java.util.List;

public class CommandsUpdatedEvent extends BaseEvent {
    public List<JamisphereCommand> commands;
    public String operation;

    @Override
    public String toString() {
        return "CommandsUpdatedEvent{}";
    }
}
