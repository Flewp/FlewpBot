package com.flewp.flewpbot.model.api;

public class JamisphereRequestUpgradeBehalfBody {
    public String request;
    public String behalfOfUserName;

    public JamisphereRequestUpgradeBehalfBody(String request, String behalfOfUserName) {
        this.request = request;
        this.behalfOfUserName = behalfOfUserName;
    }
}
