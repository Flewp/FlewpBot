package com.flewp.flewpbot.event;

import com.flewp.flewpbot.model.ChatRoom;
import com.github.philippheuer.events4j.domain.Event;

public class ChatEvent extends Event {
    private EventUser eventUser;
    private ChatRoom chatRoom;
    private String chatRoomId;
    private String message;

    public ChatEvent(EventUser eventUser, ChatRoom chatRoom, String chatRoomId, String message) {
        this.eventUser = eventUser;
        this.chatRoom = chatRoom;
        this.chatRoomId = chatRoomId;
        this.message = message;
    }

    public EventUser getEventUser() {
        return eventUser;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ChatEvent [" + eventUser.toString() + ", chatRoomID: " + chatRoomId + ", message: " + message;
    }
}
