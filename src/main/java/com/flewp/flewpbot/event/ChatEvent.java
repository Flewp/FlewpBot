package com.flewp.flewpbot.event;

import com.flewp.flewpbot.model.kraken.KrakenChatRoom;
import com.github.philippheuer.events4j.domain.Event;

public class ChatEvent extends Event {
    private EventUser eventUser;
    private KrakenChatRoom chatRoom;
    private String chatRoomId;
    private String chatRoomIrcId;
    private String message;

    public ChatEvent(EventUser eventUser, KrakenChatRoom chatRoom, String chatRoomId, String chatRoomIrcId, String message) {
        this.eventUser = eventUser;
        this.chatRoom = chatRoom;
        this.chatRoomId = chatRoomId;
        this.chatRoomIrcId = chatRoomIrcId;
        this.message = message;
    }

    public EventUser getEventUser() {
        return eventUser;
    }

    public KrakenChatRoom getChatRoom() {
        return chatRoom;
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
