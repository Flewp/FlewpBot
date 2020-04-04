package com.flewp.flewpbot.model.events.jamisphere;

import com.flewp.flewpbot.model.events.BaseEvent;

public class RequestRemovedEvent extends BaseEvent {
    public String removed;

    @Override
    public String toString() {
        return "RequestRemovedEvent{" +
                "removed='" + removed + '\'' +
                '}';
    }
}
