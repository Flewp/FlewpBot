package com.flewp.flewpbot.model.api;

import com.flewp.flewpbot.model.jamisphere.JamisphereGameEntry;
import com.flewp.flewpbot.model.jamisphere.JamisphereLogin;

public class JamisphereEventEnterResponse {
    public JamisphereGameEntry entry;
    public JamisphereLogin user;
    public String message;
}
