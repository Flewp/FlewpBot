package com.flewp.flewpbot.model.events.jamisphere;

import com.github.philippheuer.events4j.domain.Event;

public class RequestRemovedEvent extends Event {
    public String removed;

    @Override
    public String toString() {
        return "RequestRemovedEvent{" +
                "removed='" + removed + '\'' +
                '}';
    }
}
