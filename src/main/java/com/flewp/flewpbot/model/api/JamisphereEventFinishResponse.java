package com.flewp.flewpbot.model.api;

import com.flewp.flewpbot.model.jamisphere.JamisphereGame;
import com.flewp.flewpbot.model.jamisphere.JamisphereLogin;

public class JamisphereEventFinishResponse {
    public JamisphereGame event;
    public JamisphereLogin user;
    public String message;
}