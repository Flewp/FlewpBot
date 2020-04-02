package com.flewp.flewpbot.model.events.jamisphere;

import com.flewp.flewpbot.model.events.BaseEvent;
import com.flewp.flewpbot.model.jamisphere.JamisphereRequest;

public class RequestDowngradedEvent extends BaseEvent {
    public JamisphereRequest request;

    public JamisphereRequest getRequest() {
        return request;
    }
}
