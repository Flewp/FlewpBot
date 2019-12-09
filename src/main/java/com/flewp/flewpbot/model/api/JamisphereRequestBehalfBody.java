package com.flewp.flewpbot.model.api;

public class JamisphereRequestBehalfBody {
    public String request;
    public String behalfOfUserName;
    public Boolean vip;
    public Boolean playImmediately;

    public JamisphereRequestBehalfBody(String request, String behalfOfUserName, Boolean vip, Boolean playImmediately) {
        this.request = request;
        this.behalfOfUserName = behalfOfUserName;
        this.vip = vip;
        this.playImmediately = playImmediately;
    }
}
