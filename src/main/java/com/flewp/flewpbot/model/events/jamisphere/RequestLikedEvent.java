package com.flewp.flewpbot.model.events.jamisphere;

import com.github.philippheuer.events4j.domain.Event;

public class RequestLikedEvent extends Event {
    public String requestId;
    public Integer likes;
}
