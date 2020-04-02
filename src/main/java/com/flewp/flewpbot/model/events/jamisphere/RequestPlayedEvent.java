package com.flewp.flewpbot.model.events.jamisphere;

import com.flewp.flewpbot.model.events.BaseEvent;
import com.flewp.flewpbot.model.jamisphere.JamisphereRequest;

public class RequestPlayedEvent extends BaseEvent {
    public JamisphereRequest request;

    @Override
    public String toString() {
        return "RequestPlayedEvent{" +
                "request=" + request +
                '}';
    }
}
