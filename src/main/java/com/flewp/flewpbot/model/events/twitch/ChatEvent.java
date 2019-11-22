package com.flewp.flewpbot.model.events.twitch;

import com.github.philippheuer.events4j.domain.Event;

public class ChatEvent extends Event {
    private EventUser eventUser;
    private String chatRoomId;
    private String chatRoomIrcId;
    private String message;

    public ChatEvent(EventUser eventUser, String chatRoomId, String chatRoomIrcId, String message) {
        this.eventUser = eventUser;
        this.chatRoomId = chatRoomId;
        this.chatRoomIrcId = chatRoomIrcId;
        this.message = message;
    }

    public EventUser getEventUser() {
        return eventUser;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public String getChatRoomIrcId() {
        return chatRoomIrcId;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ChatEvent [ " + eventUser.toString() + ", chatRoomID: " + chatRoomId + ", message: " + message + " ]";
    }
}
