package com.flewp.flewpbot.model.api;

public class JamisphereRequestBody {
    public String request;
    public String behalfOfUserName;
    public Boolean vip;

    public JamisphereRequestBody(String request, String behalfOfUserName, Boolean vip) {
        this.request = request;
        this.behalfOfUserName = behalfOfUserName;
        this.vip = vip;
    }
}
