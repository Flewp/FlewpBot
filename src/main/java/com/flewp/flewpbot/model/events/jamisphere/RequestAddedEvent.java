package com.flewp.flewpbot.model.events.jamisphere;

import com.flewp.flewpbot.model.events.BaseEvent;
import com.flewp.flewpbot.model.jamisphere.JamisphereRequest;

public class RequestAddedEvent extends BaseEvent {
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
