package com.flewp.flewpbot;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class Configuration {
    public static Configuration getInstance() {
        File file = new File("config.yaml");
        Configuration configuration = null;

        if (file != null) {
            try {
                configuration = new Yaml().loadAs(Files.newInputStream(file.toPath()), Configuration.class);
            } catch (IOException e) {
            }
        }
        if (configuration == null) {
            throw new RuntimeException("Create a config.yaml based off the config-template.yaml and fill in the fields: looking in " + file.getAbsolutePath());
        }

        return configuration;
    }

    public String twitchAppClientID;
    public String twitchAppClientSecret;

    public String twitchStreamerAccessToken;
    public String twitchStreamerRefreshToken;
    public String twitchStreamerName;

    public String twitchChatBotAccessToken;
    public String twitchChatBotName;

    public String streamLabsClientID;
    public String streamLabsClientSecret;
    public String streamLabsAppName;
    public String streamLabsRedirectURI;

    public String streamLabsAccessToken;
    public String streamLabsRefreshToken;

    public String pusherKey;
    public String pusherCluster;

    public String discordToken;

    public boolean webBrowserAvailable;
    public boolean enableIrc = false;
    public boolean enableMidi = false;

    public boolean hasStreamlabsCredentials() {
        return streamLabsAccessToken != null && !streamLabsAccessToken.isEmpty() &&
                !streamLabsAccessToken.equalsIgnoreCase("empty") && streamLabsRefreshToken != null &&
                !streamLabsRefreshToken.isEmpty() && !streamLabsRefreshToken.equalsIgnoreCase("empty");
    }

    public boolean isStreamlabsConnectable() {
        return hasStreamlabsCredentials() || webBrowserAvailable;
    }

    public void dumpFile() {
        try {
            new Yaml().dump(this, new FileWriter("config.yaml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
