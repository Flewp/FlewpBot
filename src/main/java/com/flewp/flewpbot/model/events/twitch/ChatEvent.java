package com.flewp.flewpbot.model.events.twitch;

import com.flewp.flewpbot.model.events.BaseEvent;

public class ChatEvent extends BaseEvent {
    private EventUser eventUser;
    private String chatRoomId;
    private String message;

    public ChatEvent(EventUser eventUser, String chatRoomId, String message) {
        this.eventUser = eventUser;
        this.chatRoomId = chatRoomId;
        this.message = message;
    }

    public EventUser getEventUser() {
        return eventUser;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ChatEvent [ " + (eventUser == null ? "null" : eventUser.toString()) + ", chatRoomID: " + chatRoomId + ", message: " + message + " ]";
    }
}
