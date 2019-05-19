package com.flewp.flewpbot.event;

import com.github.philippheuer.events4j.domain.Event;

public class WhisperEvent extends Event {
    private EventUser eventUser;
    private String target;
    private String message;

    public WhisperEvent(EventUser eventUser, String target, String message) {
        this.eventUser = eventUser;
        this.target = target;
        this.message = message;
    }

    public EventUser getEventUser() {
        return eventUser;
    }

    public String getTarget() {
        return target;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "WhisperEvent [ " + eventUser.toString() + ", target: " + target + ", message: " + message + " ]";
    }
}
