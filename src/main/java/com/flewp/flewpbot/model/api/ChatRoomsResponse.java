package com.flewp.flewpbot.model.api;

import com.flewp.flewpbot.model.kraken.KrakenChatRoom;

import java.util.List;

public class ChatRoomsResponse {
    private List<KrakenChatRoom> rooms;

    private String _total;

    public List<KrakenChatRoom> getRooms() {
        return rooms;
    }

    public void setRooms(List<KrakenChatRoom> rooms) {
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
