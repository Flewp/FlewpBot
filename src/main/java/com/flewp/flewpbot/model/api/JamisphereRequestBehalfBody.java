package com.flewp.flewpbot.model.api;

public class JamisphereRequestBehalfBody {
    public String request;
    public String behalfOfUserName;
    public Boolean vip;
    public Boolean playInstantly;

    public JamisphereRequestBehalfBody(String request, String behalfOfUserName, Boolean vip, Boolean playInstantly) {
        this.request = request;
        this.behalfOfUserName = behalfOfUserName;
        this.vip = vip;
        this.playInstantly = playInstantly;
    }
}
