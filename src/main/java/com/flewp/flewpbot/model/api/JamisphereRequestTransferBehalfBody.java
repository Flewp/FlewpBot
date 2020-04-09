package com.flewp.flewpbot.model.api;

public class JamisphereRequestTransferBehalfBody {
    public String fromUserName;
    public String toUserName;

    public JamisphereRequestTransferBehalfBody(String fromUserName, String toUserName) {
        this.fromUserName = fromUserName;
        this.toUserName = toUserName;
    }
}
