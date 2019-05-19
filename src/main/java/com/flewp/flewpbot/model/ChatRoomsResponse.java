package com.flewp.flewpbot.model;

import java.util.List;

public class ChatRoomsResponse {
    private List<ChatRoom> rooms;

    private String _total;

    public List<ChatRoom> getRooms() {
        return rooms;
    }

    public void setRooms(List<ChatRoom> rooms) {
        this.rooms = rooms;
    }

    public String get_total() {
        return _total;
    }

    public void set_total(String _total) {
        this._total = _total;
    }

    @Override
    public String toString() {
        return "ChatRoomsResponse [rooms = " + rooms + ", _total = " + _total + "]";
    }
}
