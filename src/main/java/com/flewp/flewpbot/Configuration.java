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

    public String twitchClientID;
    public String twitchClientSecret;
    public String twitchAccessToken;
    public String twitchRefreshToken;
    public String streamLabsClientID;
    public String streamLabsClientSecret;
    public String streamLabsAppName;

    public void dumpFile() {
        try {
            new Yaml().dump(this, new FileWriter("config.yaml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}