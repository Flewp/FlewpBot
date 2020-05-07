package com.flewp.flewpbot.model.events.twitch.pubsub;

import java.util.List;

public class ListenData {
    public List<String> topics;
    public String auth_token;

    public ListenData(List<String> topics, String auth_token) {
        this.topics = topics;
        this.auth_token = auth_token;
    }
}
