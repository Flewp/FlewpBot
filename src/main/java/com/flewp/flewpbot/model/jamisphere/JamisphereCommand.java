package com.flewp.flewpbot.model.jamisphere;

public class JamisphereCommand {
    public String name;
    public String description;
    public String execution;
    public Boolean modOnly;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getExecution() {
        return execution;
    }

    public Boolean getModOnly() {
        return modOnly;
    }
}
