package com.flewp.flewpbot.model.streamlabs;

public class StreamlabsCredential {
    public String access_token;
    public String token_type;
    public String refresh_token;

    public StreamlabsCredential(String access_token, String token_type, String refresh_token) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.refresh_token = refresh_token;
    }
}
