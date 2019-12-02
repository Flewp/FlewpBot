package com.flewp.flewpbot.model.api;

import com.google.gson.JsonObject;

public class JamispherePusherBody {
    public JsonObject data;
    public String eventType;

    public JamispherePusherBody(JsonObject data, String eventType) {
        this.data = data;
        this.eventType = eventType;
    }
}
