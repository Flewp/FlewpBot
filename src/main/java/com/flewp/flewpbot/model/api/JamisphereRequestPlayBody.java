package com.flewp.flewpbot.model.api;

public class JamisphereRequestPlayBody {
    private String userName;
    private String videoId;

    public JamisphereRequestPlayBody(String userName, String videoId) {
        this.userName = userName;
        this.videoId = videoId;
    }
}
