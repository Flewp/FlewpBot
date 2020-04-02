package com.flewp.flewpbot.model.events.jamisphere;

import com.flewp.flewpbot.model.events.BaseEvent;

public class RequestUnlikedEvent extends BaseEvent {
    public String requestId;
    public Integer likes;

    @Override
    public String toString() {
        return "RequestUnlikedEvent{" +
                "requestId='" + requestId + '\'' +
                ", likes=" + likes +
                '}';
    }
}
