package com.flewp.flewpbot.api;

import com.flewp.flewpbot.Configuration;
import com.flewp.flewpbot.event.NewDonationEvent;
import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.streamlabs4j.StreamlabsClient;
import com.github.twitch4j.streamlabs4j.StreamlabsClientBuilder;
import com.github.twitch4j.streamlabs4j.api.domain.StreamlabsDonationsData;
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
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StreamlabsAPIController {
    private Configuration configuration;
    private EventManager eventManager;

    private Credential credential;
    private StreamlabsClient streamlabsClient;
    private ScheduledExecutorService service;

    private Long previousDonation = 1L;

    public StreamlabsAPIController(Configuration configuration, EventManager eventManager) {
        this.configuration = configuration;
        this.eventManager = eventManager;
    }

    public synchronized void connect() {
        try {
            DataStoreFactory dataStoreFactory = new FileDataStoreFactory(new File("Data_Store_StreamLabs"));
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
                    "https://www.streamlabs.com/api/v1.0/authorize").setScopes(Collections.singletonList("donations.read"))
                    .setDataStoreFactory(dataStoreFactory).build();
            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setHost("127.0.0.1").setPort(11111).build();
            credential = new AuthorizationCodeInstalledApp(flow, receiver)
                    .authorize(configuration.streamLabsAppName);

            if (credential == null) {
                throw new GeneralSecurityException("credential is null.");
            }

            credential.refreshToken();
            credential.setExpirationTimeMilliseconds((long) 1800 * 1000);

            streamlabsClient = StreamlabsClientBuilder.builder()
                    .withClientId(client_id)
                    .withClientSecret(client_secret)
                    .build();

            //getNewDonations();

            service = Executors.newScheduledThreadPool(1);
            service.scheduleAtFixedRate(() -> {
                StreamlabsDonationsData newDonations = getNewDonations();
                if (newDonations != null && newDonations.getDonations() != null && !newDonations.getDonations().isEmpty()) {
                    newDonations.getDonations().forEach(donation ->
                            eventManager.dispatchEvent(new NewDonationEvent(donation)));
                }
            }, 10, 10, TimeUnit.SECONDS);
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    private StreamlabsDonationsData getNewDonations() {
        StreamlabsDonationsData response = streamlabsClient.getApi()
                .getDonations(credential.getAccessToken(), 6, null, Long.toString(previousDonation), "USD", null).execute();

        if (response == null || response.getDonations() == null || response.getDonations().isEmpty()) {
            return null;
        }

        List<StreamlabsDonationsData.StreamlabsDonation> donations = response.getDonations().subList(0, response.getDonations().size());
        Collections.reverse(donations);

        previousDonation = response.getDonations().get(response.getDonations().size() - 1)
                .getCreationDate().getTime();

        return response;
    }
}
