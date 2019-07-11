package com.flewp.flewpbot.api.interceptor;

import com.flewp.flewpbot.Configuration;
import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;

public class JamisphereRequestInterceptor implements Interceptor {
    private Configuration configuration;

    public JamisphereRequestInterceptor(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(chain.request().newBuilder()
                .addHeader("token", configuration.twitchStreamerAccessToken).build());
    }
}
