package com.flewp.flewpbot.model.events.twitch.pubsub;

import com.google.gson.JsonObject;

public class PubsubEvent {
    public String type;
    public String nonce;
    public JsonObject data;

    public PubsubEvent(String type, String nonce, JsonObject data) {
        this.type = type;
        this.nonce = nonce;
        this.data = data;
    }

    @Override
    public String toString() {
        return "PubsubEvent{" +
                "type='" + type + '\'' +
                ", nonce='" + nonce + '\'' +
                ", data=" + data +
                '}';
    }
}
