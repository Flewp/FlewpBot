package com.flewp.flewpbot.api.interceptor;

import com.flewp.flewpbot.Configuration;
import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;

public class TwitchKrakenRequestInterceptor implements Interceptor {
    private Configuration configuration;

    public TwitchKrakenRequestInterceptor(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(chain.request().newBuilder()
                .addHeader("Accept", "application/vnd.twitchtv.v5+json")
                .addHeader("Authorization", "OAuth " + configuration.twitchStreamerAccessToken)
                .addHeader("Client-ID", configuration.twitchAppClientID)
                .build());
    }
}
