package com.flewp.flewpbot.model.api;

import java.util.List;

public class TwitchTokenResponse {
    public String access_token;
    public String refresh_token;
    public Integer expires_in;
    public List<String> scope;
    public String token_type;
}
