package com.flewp.flewpbot.model.api;

import com.flewp.flewpbot.model.jamisphere.JamisphereRequest;

public class JamisphereRequestUpgradeResponse extends JamisphereBaseResponse {
    public JamisphereRequest request;
    public Boolean upgradeAvailable;
    public String upgradeStatus;
}
