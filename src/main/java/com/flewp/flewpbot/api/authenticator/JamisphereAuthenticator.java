package com.flewp.flewpbot.api.authenticator;

import com.flewp.flewpbot.Configuration;
import com.flewp.flewpbot.api.controller.TwitchAPIController;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

import javax.annotation.Nullable;
import java.io.IOException;

public class JamisphereAuthenticator implements Authenticator {
    private Configuration configuration;

    public JamisphereAuthenticator(Configuration configuration) {
        this.configuration = configuration;
    }

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, Response response) throws IOException {
        TwitchAPIController.refreshCredentials(configuration);

        return response.request().newBuilder()
                .removeHeader("token")
                .addHeader("token", configuration.twitchStreamerAccessToken)
                .build();
    }
}
