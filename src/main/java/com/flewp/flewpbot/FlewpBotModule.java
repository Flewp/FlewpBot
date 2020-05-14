package com.flewp.flewpbot;

import com.flewp.flewpbot.api.*;
import com.flewp.flewpbot.api.authenticator.JamisphereAuthenticator;
import com.flewp.flewpbot.api.authenticator.StreamlabsAuthenticator;
import com.flewp.flewpbot.api.authenticator.TwitchAuthenticator;
import com.flewp.flewpbot.api.controller.DiscordAPIController;
import com.flewp.flewpbot.api.controller.StreamlabsAPIController;
import com.flewp.flewpbot.api.controller.TwitchAPIController;
import com.flewp.flewpbot.api.controller.TwitchPubSubController;
import com.flewp.flewpbot.api.interceptor.JamisphereRequestInterceptor;
import com.flewp.flewpbot.api.interceptor.StreamlabsRequestInterceptor;
import com.flewp.flewpbot.api.interceptor.TwitchRequestInterceptor;
import com.flewp.flewpbot.pusher.PusherManager;
import com.tinder.scarlet.Scarlet;
import com.tinder.scarlet.lifecycle.LifecycleRegistry;
import com.tinder.scarlet.messageadapter.gson.GsonMessageAdapter;
import com.tinder.scarlet.websocket.okhttp.OkHttpClientUtils;
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
    public JamisphereAuthenticator provideJamisphereAuthenticator(Configuration configuration) {
        return new JamisphereAuthenticator(configuration);
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
                                              JamisphereRequestInterceptor requestInterceptor,
                                              JamisphereAuthenticator authenticator) {
        return new Retrofit.Builder()
                .baseUrl("https://hqc73tr82k.execute-api.us-west-2.amazonaws.com/Alpha/")
                .client(okHttpClient.newBuilder()
                        .addInterceptor(requestInterceptor)
                        .authenticator(authenticator)
                        .build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(JamisphereAPI.class);
    }

    @Provides
    @Singleton
    public TwitchPubsubAPI provideTwitchPubsubAPI(OkHttpClient okHttpClient, LifecycleRegistry lifecycleRegistry) {
        return new Scarlet.Builder()
                .webSocketFactory(OkHttpClientUtils.newWebSocketFactory(okHttpClient, "wss://pubsub-edge.twitch.tv"))
                .addMessageAdapterFactory(new GsonMessageAdapter.Factory())
                .lifecycle(lifecycleRegistry)
                .build()
                .create(TwitchPubsubAPI.class);
    }

    @Provides
    @Singleton
    public TwitchAuthAPI provideTwitchAuthAPI(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl("https://id.twitch.tv/")
                .client(okHttpClient.newBuilder().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TwitchAuthAPI.class);
    }

    @Provides
    @Singleton
    public TwitchPubSubController provideTwitchPubSubController(Configuration configuration, EventManager eventManager,
                                                                LifecycleRegistry lifecycleRegistry, TwitchPubsubAPI twitchPubsubAPI,
                                                                JamisphereAPI jamisphereAPI) {
        return new TwitchPubSubController(configuration, eventManager, lifecycleRegistry, twitchPubsubAPI, jamisphereAPI);
    }

    @Provides
    @Singleton
    public TwitchAPIController provideTwitchAPIController(Configuration configuration, EventManager eventManager, TwitchAPI twitchAPI, JamisphereAPI jamisphereAPI) {
        return new TwitchAPIController(configuration, eventManager, twitchAPI, jamisphereAPI);
    }

    @Provides
    @Singleton
    public StreamlabsAPIController provideStreamlabsAPIController(Configuration configuration, EventManager eventManager,
                                                                  StreamlabsAPI streamlabsAPI, JamisphereAPI jamisphereAPI) {
        return new StreamlabsAPIController(configuration, eventManager, streamlabsAPI, jamisphereAPI);
    }

    @Provides
    @Singleton
    public DiscordAPIController provideDiscordAPIController(Configuration configuration, EventManager eventManager) {
        return new DiscordAPIController(configuration, eventManager);
    }

    @Provides
    @Singleton
    public PusherManager providePusherManager(Configuration configuration, EventManager eventManager) {
        return new PusherManager(configuration, eventManager);
    }

    @Provides
    @Singleton
    public LifecycleRegistry provideLifecycleRegistry() {
        return new LifecycleRegistry(0L);
    }
}
