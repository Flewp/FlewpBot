package com.flewp.flewpbot.api.interceptor;

import com.flewp.flewpbot.Configuration;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class StreamlabsRequestInterceptor implements Interceptor {
    private Configuration configuration;

    public StreamlabsRequestInterceptor(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        builder.url(chain.request().url().newBuilder()
                .addQueryParameter("access_token", configuration.streamLabsAccessToken).build());
        return chain.proceed(builder.build());
    }
}
