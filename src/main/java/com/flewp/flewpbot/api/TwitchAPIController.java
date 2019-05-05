package com.flewp.flewpbot.api;

import com.flewp.flewpbot.Configuration;
import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.CredentialManagerBuilder;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TwitchAPIController {
    private Configuration configuration;
    private TwitchTokenGeneratorAPI generatorAPI;
    private EventManager eventManager;

    public TwitchAPIController(Configuration configuration, TwitchTokenGeneratorAPI generatorAPI, EventManager eventManager) {
        this.configuration = configuration;
        this.generatorAPI = generatorAPI;
        this.eventManager = eventManager;
    }

    public synchronized void connect() {
        try {
            generatorAPI.refreshToken(configuration.twitchRefreshToken).enqueue(new Callback<RefreshTokenResponse>() {
                @Override
                public void onResponse(@NotNull Call<RefreshTokenResponse> call, @NotNull Response<RefreshTokenResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().success) {
                        configuration.twitchAccessToken = response.body().token;
                        configuration.twitchRefreshToken = response.body().refresh;
                        configuration.dumpFile();
                    }

                    init();
                }

                @Override
                public void onFailure(@NotNull Call<RefreshTokenResponse> call, @NotNull Throwable throwable) {
                    init();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        OAuth2Credential twitchOAuth2Credential = new OAuth2Credential("twitch", configuration.twitchAccessToken);

        CredentialManager twitchCredentialManager = CredentialManagerBuilder.builder().build();
        twitchCredentialManager.registerIdentityProvider(new TwitchIdentityProvider(configuration.twitchClientID,
                configuration.twitchClientSecret, "http://localhost:11112/Callback"));
        twitchCredentialManager.addCredential("twitch",
                twitchOAuth2Credential);


        TwitchClient twitchClient = TwitchClientBuilder.builder()
                .withEnablePubSub(true)
                .withEnableHelix(true)
                .withEnableKraken(true)
                .withEnableChat(true)
                .withClientId(configuration.twitchClientID)
                .withClientSecret(configuration.twitchClientSecret)
                .withChatAccount(twitchOAuth2Credential)
                .withCredentialManager(twitchCredentialManager)
                .withEventManager(eventManager)
                .build();

        twitchClient.getChat().joinChannel("flewp");
    }
}
