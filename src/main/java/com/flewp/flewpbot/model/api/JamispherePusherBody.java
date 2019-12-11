package com.flewp.flewpbot.model.api;

import com.google.gson.JsonObject;

public class JamispherePusherBody {
    public JsonObject data;
    public String channel;
    public String eventType;

    public JamispherePusherBody(JsonObject data, String channel, String eventType) {
        this.data = data;
        this.channel = channel;
        this.eventType = eventType;
    }
}
