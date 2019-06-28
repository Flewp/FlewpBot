package com.flewp.flewpbot.api.authenticator;

import com.flewp.flewpbot.Configuration;
import com.flewp.flewpbot.api.controller.StreamlabsAPIController;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

import javax.annotation.Nullable;

public class StreamlabsAuthenticator implements Authenticator {
    private Configuration configuration;

    public StreamlabsAuthenticator(Configuration configuration) {
        this.configuration = configuration;
    }

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, Response response) {
        StreamlabsAPIController.refreshCredentials(configuration);

        Request.Builder builder = response.request().newBuilder();
        builder.url(response.request().url().newBuilder()
                .addQueryParameter("access_token", configuration.streamLabsAccessToken).build());
        return builder.build();
    }
}
