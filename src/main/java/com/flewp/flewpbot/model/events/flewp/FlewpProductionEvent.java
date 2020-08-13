package com.flewp.flewpbot.model.events.flewp;

import com.flewp.flewpbot.model.events.BaseEvent;
import com.google.gson.JsonObject;

public class FlewpProductionEvent extends BaseEvent {
    public JsonObject data;

    public FlewpProductionEvent(JsonObject data) {
        this.data = data;
    }

    public String getProductionValue() {
        if (data == null || data.get("production") == null || !data.get("production").isJsonPrimitive() ||
                !data.get("production").getAsJsonPrimitive().isString()) {
            return "";
        }

        return data.get("production").getAsString();
    }
}
