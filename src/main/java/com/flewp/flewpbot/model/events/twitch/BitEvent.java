package com.flewp.flewpbot.model.events.twitch;

import com.flewp.flewpbot.model.events.BaseEvent;

public class BitEvent extends BaseEvent {
    private EventUser eventUser;
    private String message;
    private int bits;

    public BitEvent(EventUser eventUser, String message, int bits) {
        this.eventUser = eventUser;
        this.message = message;
        this.bits = bits;
    }

    public EventUser getEventUser() {
        return eventUser;
    }

    public String getMessage() {
        return message;
    }

    public int getBits() {
        return bits;
    }

    @Override
    public String toString() {
        return "BitEvent [ " + eventUser.toString() + ", bits: " + bits + ", message: " + message + " ]";
    }
}
