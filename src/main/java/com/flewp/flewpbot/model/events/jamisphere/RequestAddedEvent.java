package com.flewp.flewpbot.model.events.jamisphere;

import com.flewp.flewpbot.model.jamisphere.JamisphereRequest;
import com.github.philippheuer.events4j.domain.Event;

public class RequestAddedEvent extends Event {
    public JamisphereRequest request;
    public String removed;

    @Override
    public String toString() {
        return "RequestAddedEvent{" +
                "request=" + request +
                ", removed='" + removed + '\'' +
                '}';
    }
}
