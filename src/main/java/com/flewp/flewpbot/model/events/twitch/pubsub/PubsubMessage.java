package com.flewp.flewpbot.model.events.twitch.pubsub;

import com.google.gson.JsonObject;

public class PubsubMessage {
    public String topic;
    public String message;

    public class Data {
        public String type;
        public JsonObject data;
    }
}
