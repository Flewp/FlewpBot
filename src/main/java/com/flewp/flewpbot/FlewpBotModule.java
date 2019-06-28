package com.flewp.flewpbot;

import com.flewp.flewpbot.api.StreamlabsAPI;
import com.flewp.flewpbot.api.TwitchHelixAPI;
import com.flewp.flewpbot.api.TwitchKrakenAPI;
import com.flewp.flewpbot.api.authenticator.StreamlabsAuthenticator;
import com.flewp.flewpbot.api.authenticator.TwitchHelixAuthenticator;
import com.flewp.flewpbot.api.authenticator.TwitchKrakenAuthenticator;
import com.flewp.flewpbot.api.controller.StreamlabsAPIController;
import com.flewp.flewpbot.api.controller.TwitchAPIController;
import com.flewp.flewpbot.api.interceptor.StreamlabsRequestInterceptor;
import com.flewp.flewpbot.api.interceptor.TwitchHelixRequestInterceptor;
import com.flewp.flewpbot.api.interceptor.TwitchKrakenRequestInterceptor;
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
    public TwitchHelixRequestInterceptor provideTwitchHelixRequestInterceptor(Configuration configuration) {
        return new TwitchHelixRequestInterceptor(configuration);
    }

    @Provides
    @Singleton
    public TwitchKrakenRequestInterceptor provideTwitchKrakenRequestInterceptor(Configuration configuration) {
        return new TwitchKrakenRequestInterceptor(configuration);
    }

    @Provides
    @Singleton
    public StreamlabsRequestInterceptor provideStreamlabsRequestInterceptor(Configuration configuration) {
        return new StreamlabsRequestInterceptor(configuration);
    }

    @Provides
    @Singleton
    public TwitchHelixAuthenticator provideTwitchHelixAuthenticator(Configuration configuration) {
        return new TwitchHelixAuthenticator(configuration);
    }

    @Provides
    @Singleton
    public TwitchKrakenAuthenticator provideTwitchKrakenAuthenticator(Configuration configuration) {
        return new TwitchKrakenAuthenticator(configuration);
    }

    @Provides
    @Singleton
    public StreamlabsAuthenticator provideStreamlabsAuthenticator(Configuration configuration) {
        return new StreamlabsAuthenticator(configuration);
    }

    @Provides
    @Singleton
    public TwitchHelixAPI provideTwitchHelixAPI(OkHttpClient okHttpClient,
                                                TwitchHelixRequestInterceptor requestInterceptor,
                                                TwitchHelixAuthenticator authenticator) {
        return new Retrofit.Builder()
                .baseUrl("https://api.twitch.tv/helix/")
                .client(okHttpClient.newBuilder()
                        .addInterceptor(requestInterceptor)
                        .authenticator(authenticator)
                        .build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(TwitchHelixAPI.class);
    }

    @Provides
    @Singleton
    public TwitchKrakenAPI provideTwitchKrakenAPI(OkHttpClient okHttpClient,
                                                  TwitchKrakenRequestInterceptor requestInterceptor,
                                                  TwitchKrakenAuthenticator authenticator) {
        return new Retrofit.Builder()
                .baseUrl("https://api.twitch.tv/kraken/")
                .client(okHttpClient.newBuilder()
                        .addInterceptor(requestInterceptor)
                        .authenticator(authenticator)
                        .build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(TwitchKrakenAPI.class);
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
    public TwitchAPIController provideTwitchAPIController(Configuration configuration, EventManager eventManager,
                                                          TwitchKrakenAPI twitchKrakenAPI, TwitchHelixAPI twitchHelixAPI) {
        return new TwitchAPIController(configuration, eventManager, twitchKrakenAPI, twitchHelixAPI);
    }

    @Provides
    @Singleton
    public StreamlabsAPIController provideStreamlabsAPIController(Configuration configuration, EventManager eventManager,
                                                                  StreamlabsAPI streamlabsAPI) {
        return new StreamlabsAPIController(configuration, eventManager, streamlabsAPI);
    }
}
