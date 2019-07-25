package com.flewp.flewpbot.model.api;

import java.util.List;

public class JamisphereDrumsBody {
    public String requestId;
    public List<Hit> hits;

    public JamisphereDrumsBody(String requestId, List<Hit> hits) {
        this.requestId = requestId;
        this.hits = hits;
    }

    public static class Hit {
        public int midi;
        public long timestamp;

        public Hit(int midi, long timestamp) {
            this.midi = midi;
            this.timestamp = timestamp;
        }
    }
}
