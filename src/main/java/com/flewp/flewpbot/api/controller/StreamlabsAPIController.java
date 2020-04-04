package com.flewp.flewpbot.api.controller;

import com.flewp.flewpbot.Configuration;
import com.flewp.flewpbot.EventManager;
import com.flewp.flewpbot.api.JamisphereAPI;
import com.flewp.flewpbot.api.RetrofitEmptyCallback;
import com.flewp.flewpbot.api.StreamlabsAPI;
import com.flewp.flewpbot.api.StreamlabsTokenAPI;
import com.flewp.flewpbot.model.api.GetDonationsResponse;
import com.flewp.flewpbot.model.api.JamispherePusherBody;
import com.flewp.flewpbot.model.api.RefreshStreamlabsTokenResponse;
import com.flewp.flewpbot.model.events.twitch.NewDonationEvent;
import com.flewp.flewpbot.model.streamlabs.StreamlabsDonation;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import org.slf4j.LoggerFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StreamlabsAPIController {
    private Gson gson = new Gson();

    private Configuration configuration;
    private EventManager eventManager;
    private StreamlabsAPI streamlabsAPI;
    private JamisphereAPI jamisphereAPI;

    private Integer previousDonation = 1;

    public StreamlabsAPIController(Configuration configuration, EventManager eventManager, StreamlabsAPI streamlabsAPI, JamisphereAPI jamisphereAPI) {
        this.configuration = configuration;
        this.eventManager = eventManager;
        this.streamlabsAPI = streamlabsAPI;
        this.jamisphereAPI = jamisphereAPI;

        if (!configuration.isStreamlabsConnectable()) {
            LoggerFactory.getLogger(StreamlabsAPIController.class).info("StreamLabs credentials not provided. StreamLabs will not be connected");
            return;
        }
    }

    public static void refreshCredentials(Configuration configuration) {
        String accessToken = null;
        String refreshToken = null;

        if (!configuration.isStreamlabsConnectable()) {
            return;
        }

        try {
            if (configuration.hasStreamlabsCredentials()) {
                StreamlabsTokenAPI streamlabsTokenAPI = new Retrofit.Builder()
                        .baseUrl("https://streamlabs.com/api/v1.0/")
                        .client(new OkHttpClient())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build().create(StreamlabsTokenAPI.class);

                Response<RefreshStreamlabsTokenResponse> response = streamlabsTokenAPI.refreshToken(
                        "refresh_token", configuration.streamLabsClientID,
                        configuration.streamLabsClientSecret, configuration.streamLabsRedirectURI).execute();

                if (response.isSuccessful() && response.body() != null) {
                    accessToken = response.body().access_token;
                    refreshToken = response.body().refresh_token;
                    LoggerFactory.getLogger(StreamlabsAPIController.class).info("Refreshed Streamlabs token");
                } else if (!response.isSuccessful() && configuration.webBrowserAvailable) {
                    LoggerFactory.getLogger(StreamlabsAPIController.class).warn("Refreshing Streamlabs token unsuccessful, falling back on web browser");
                } else {
                    throw new IllegalStateException("Streamlabs token are invalid, and no web browser to fall back on, please refresh credentials");
                }
            }

            if (!configuration.hasStreamlabsCredentials() && configuration.webBrowserAvailable) {
                JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();

                String client_id = configuration.streamLabsClientID;
                String client_secret = configuration.streamLabsClientSecret;

                HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
                AuthorizationCodeFlow flow = new AuthorizationCodeFlow.Builder(BearerToken
                        .authorizationHeaderAccessMethod(),
                        httpTransport,
                        jsonFactory,
                        new GenericUrl("https://streamlabs.com/api/v1.0/token"),
                        new ClientParametersAuthentication(
                                client_id, client_secret),
                        client_id,
                        "https://www.streamlabs.com/api/v1.0/authorize")
                        .setScopes(Collections.singletonList("donations.read")).build();

                LocalServerReceiver receiver = new LocalServerReceiver.Builder().setHost("127.0.0.1").setPort(11111).build();

                Credential credential = new AuthorizationCodeInstalledApp(flow, receiver)
                        .authorize(configuration.streamLabsAppName);

                if (credential == null) {
                    throw new GeneralSecurityException("Credential is null after trying authorization flow for Streamlabs.");
                }

                accessToken = credential.getAccessToken();
                refreshToken = credential.getRefreshToken();
            }
        } catch (IOException | GeneralSecurityException e) {
            LoggerFactory.getLogger(StreamlabsAPIController.class).error("Error refreshing Streamlabs credentials: " + e.getMessage());
        }

        if (accessToken == null || refreshToken == null) {
            LoggerFactory.getLogger(StreamlabsAPIController.class).error("Access and Refresh tokens for Streamlabs are in a bad state, please restart with fresh Streamlabs credentials.");
            return;
        }

        configuration.streamLabsAccessToken = accessToken;
        configuration.streamLabsRefreshToken = refreshToken;
        configuration.dumpFile();
    }

    public synchronized void startQueryingDonations() {
        if (!configuration.isStreamlabsConnectable()) {
            return;
        }

        getNewDonations();

        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(() -> {
            List<StreamlabsDonation> newDonations = getNewDonations();
            if (newDonations != null && !newDonations.isEmpty()) {
                newDonations.forEach(donation -> {
                    NewDonationEvent event = new NewDonationEvent(donation);
                    jamisphereAPI.pusher(new JamispherePusherBody(gson.toJsonTree(event).getAsJsonObject(),
                            "jamisphere", "streamlabsDonation")).enqueue(new RetrofitEmptyCallback<>());
                    eventManager.dispatchEvent(event);
                });
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    private List<StreamlabsDonation> getNewDonations() {
        try {
            Response<GetDonationsResponse> response =
                    streamlabsAPI.getDonations(configuration.streamLabsAccessToken, previousDonation, null).execute();

            if (!response.isSuccessful() || response.body() == null || response.body().data == null) {
                LoggerFactory.getLogger(StreamlabsAPIController.class).info("Unable to get donations from Streamlabs");
                return null;
            }

            if (response.body().data.isEmpty()) {
                return response.body().data;
            }

            previousDonation = Integer.parseInt(response.body().data.get(0).donation_id);

            return response.body().data;
        } catch (Exception e) {
            LoggerFactory.getLogger(StreamlabsAPIController.class).error("Unable to get donations from Streamlabs: " + e.getMessage());
        }

        return new ArrayList<>();
    }

    public StreamlabsAPI getStreamlabsAPI() {
        if (!configuration.isStreamlabsConnectable()) {
            throw new IllegalStateException("The StreamLabs credentials are not setup, cannot get API.");
        }

        return streamlabsAPI;
    }
}
