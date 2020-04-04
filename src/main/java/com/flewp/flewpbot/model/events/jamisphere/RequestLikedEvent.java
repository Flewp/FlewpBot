package com.flewp.flewpbot.model.events.jamisphere;

import com.flewp.flewpbot.model.events.BaseEvent;

public class RequestLikedEvent extends BaseEvent {
    public String requestId;
    public Integer likes;

    @Override
    public String toString() {
        return "RequestLikedEvent{" +
                "requestId='" + requestId + '\'' +
                ", likes=" + likes +
                '}';
    }
}
