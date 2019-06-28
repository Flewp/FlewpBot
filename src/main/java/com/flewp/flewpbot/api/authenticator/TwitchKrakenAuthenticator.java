package com.flewp.flewpbot.api.authenticator;

import com.flewp.flewpbot.Configuration;
import com.flewp.flewpbot.api.controller.TwitchAPIController;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

import javax.annotation.Nullable;

public class TwitchKrakenAuthenticator implements Authenticator {
    private Configuration configuration;

    public TwitchKrakenAuthenticator(Configuration configuration) {
        this.configuration = configuration;
    }

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, Response response) {
        TwitchAPIController.refreshCredentials(configuration);

        return response.request().newBuilder()
                .addHeader("Authorization", "OAuth " + configuration.twitchStreamerAccessToken)
                .build();
    }
}
