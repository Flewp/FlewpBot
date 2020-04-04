package com.flewp.flewpbot.model.api;

import com.flewp.flewpbot.model.jamisphere.JamisphereCommand;

import java.util.List;

public class JamisphereCommandsBody {
    public String operation;
    public List<JamisphereCommand> commands;

    public JamisphereCommandsBody(String operation, List<JamisphereCommand> commands) {
        this.operation = operation;
        this.commands = commands;
    }
}
