package com.flewp.flewpbot.api.authenticator;

import com.flewp.flewpbot.Configuration;
import com.flewp.flewpbot.api.controller.TwitchAPIController;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

import javax.annotation.Nullable;

public class TwitchAuthenticator implements Authenticator {
    private Configuration configuration;

    public TwitchAuthenticator(Configuration configuration) {
        this.configuration = configuration;
    }

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, Response response) {
        TwitchAPIController.refreshCredentials(configuration);

        return response.request().newBuilder()
                .removeHeader("Authorization")
                .addHeader("Authorization", "Bearer " + configuration.twitchStreamerAccessToken)
                .build();
    }
}
