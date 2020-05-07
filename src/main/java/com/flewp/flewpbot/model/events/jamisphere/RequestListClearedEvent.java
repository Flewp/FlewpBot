package com.flewp.flewpbot.model.events.jamisphere;

import com.flewp.flewpbot.model.events.BaseEvent;

public class RequestListClearedEvent extends BaseEvent {

    public Boolean cleared;

    @Override
    public String toString() {
        return "RequestListClearedEvent{}";
    }
}
