package com.flewp.flewpbot.api;

import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;

public class TwitchKrakenRequestInterceptor implements Interceptor {
    private String clientID;
    private String streamerAccessToken;

    public TwitchKrakenRequestInterceptor(String clientID, String streamerAccessToken) {
        this.clientID = clientID;
        this.streamerAccessToken = streamerAccessToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(chain.request().newBuilder()
                .addHeader("Accept", "application/vnd.twitchtv.v5+json")
                .addHeader("Authorization", "OAuth " + streamerAccessToken)
                .addHeader("Client-ID", clientID)
                .build());
    }
}
