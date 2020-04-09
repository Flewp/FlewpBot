package com.flewp.flewpbot.model.api;

public class JamisphereRequestUpgradeBehalfBody {
    public String behalfOfUserName;
    public boolean forceUpgrade;

    public JamisphereRequestUpgradeBehalfBody(String behalfOfUserName, boolean forceUpgrade) {
        this.behalfOfUserName = behalfOfUserName;
        this.forceUpgrade = forceUpgrade;
    }
}
