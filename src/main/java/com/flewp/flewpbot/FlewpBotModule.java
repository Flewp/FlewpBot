package com.flewp.flewpbot;

import com.flewp.flewpbot.api.JamisphereAPI;
import com.flewp.flewpbot.api.StreamlabsAPI;
import com.flewp.flewpbot.api.TwitchAPI;
import com.flewp.flewpbot.api.authenticator.StreamlabsAuthenticator;
import com.flewp.flewpbot.api.authenticator.TwitchAuthenticator;
import com.flewp.flewpbot.api.controller.StreamlabsAPIController;
import com.flewp.flewpbot.api.controller.TwitchAPIController;
import com.flewp.flewpbot.api.interceptor.JamisphereRequestInterceptor;
import com.flewp.flewpbot.api.interceptor.StreamlabsRequestInterceptor;
import com.flewp.flewpbot.api.interceptor.TwitchRequestInterceptor;
import com.flewp.flewpbot.pusher.PusherManager;
import com.github.philippheuer.events4j.EventManager;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import javax.inject.Singleton;

@Module
class FlewpBotModule {
    private Configuration configuration;

    public FlewpBotModule(Configuration configuration) {
        this.configuration = configuration;
    }

    @Provides
    @Singleton
    public Configuration provideConfiguration() {
        return configuration;
    }

    @Provides
    @Singleton
    public EventManager provideEventManager() {
        return new EventManager();
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        return new OkHttpClient();
    }

    @Provides
    @Singleton
    public TwitchRequestInterceptor provideTwitchRequestInterceptor(Configuration configuration) {
        return new TwitchRequestInterceptor(configuration);
    }

    @Provides
    @Singleton
    public StreamlabsRequestInterceptor provideStreamlabsRequestInterceptor(Configuration configuration) {
        return new StreamlabsRequestInterceptor(configuration);
    }

    @Provides
    @Singleton
    public JamisphereRequestInterceptor provideJamisphereRequestInterceptor(Configuration configuration) {
        return new JamisphereRequestInterceptor(configuration);
    }

    @Provides
    @Singleton
    public TwitchAuthenticator provideTwitchAuthenticator(Configuration configuration) {
        return new TwitchAuthenticator(configuration);
    }

    @Provides
    @Singleton
    public StreamlabsAuthenticator provideStreamlabsAuthenticator(Configuration configuration) {
        return new StreamlabsAuthenticator(configuration);
    }

    @Provides
    @Singleton
    public TwitchAPI provideTwitchAPI(OkHttpClient okHttpClient,
                                           TwitchRequestInterceptor requestInterceptor,
                                           TwitchAuthenticator authenticator) {
        return new Retrofit.Builder()
                .baseUrl("https://api.twitch.tv/helix/")
                .client(okHttpClient.newBuilder()
                        .addInterceptor(requestInterceptor)
                        .authenticator(authenticator)
                        .build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(TwitchAPI.class);
    }

    @Provides
    @Singleton
    public StreamlabsAPI provideStreamlabsAPI(OkHttpClient okHttpClient,
                                              StreamlabsRequestInterceptor requestInterceptor,
                                              StreamlabsAuthenticator authenticator) {
        return new Retrofit.Builder()
                .baseUrl("https://streamlabs.com/api/v1.0/")
                .client(okHttpClient.newBuilder()
                        .addInterceptor(requestInterceptor)
                        .authenticator(authenticator)
                        .build())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(StreamlabsAPI.class);
    }

    @Provides
    @Singleton
    public JamisphereAPI provideJamisphereAPI(OkHttpClient okHttpClient,
                                              JamisphereRequestInterceptor requestInterceptor) {
        return new Retrofit.Builder()
                .baseUrl("https://hqc73tr82k.execute-api.us-west-2.amazonaws.com/Alpha/")
                .client(okHttpClient.newBuilder()
                        .addInterceptor(requestInterceptor)
                        .build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(JamisphereAPI.class);
    }

    @Provides
    @Singleton
    public TwitchAPIController provideTwitchAPIController(Configuration configuration, EventManager eventManager, TwitchAPI twitchAPI) {
        return new TwitchAPIController(configuration, eventManager, twitchAPI);
    }

    @Provides
    @Singleton
    public StreamlabsAPIController provideStreamlabsAPIController(Configuration configuration, EventManager eventManager,
                                                                  StreamlabsAPI streamlabsAPI) {
        return new StreamlabsAPIController(configuration, eventManager, streamlabsAPI);
    }

    @Provides
    @Singleton
    public PusherManager providePusherManager(Configuration configuration, EventManager eventManager) {
        return new PusherManager(configuration, eventManager);
    }
}
