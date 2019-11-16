package com.flewp.flewpbot.model.events.jamisphere;

import com.flewp.flewpbot.model.jamisphere.JamisphereRequest;
import com.github.philippheuer.events4j.domain.Event;

public class RequestUpgradedEvent extends Event {
    public JamisphereRequest request;
}
