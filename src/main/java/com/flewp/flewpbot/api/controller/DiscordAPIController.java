package com.flewp.flewpbot.api.controller;

import com.flewp.flewpbot.Configuration;
import com.flewp.flewpbot.EventManager;
import com.flewp.flewpbot.model.events.twitch.ChatEvent;
import com.flewp.flewpbot.model.events.twitch.MessageOrigin;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public class DiscordAPIController extends ListenerAdapter {

    private Configuration configuration;
    private EventManager eventManager;

    private JDA jda;

    public DiscordAPIController(Configuration configuration, EventManager eventManager) {
        this.configuration = configuration;
        this.eventManager = eventManager;
    }

    public void start() {
        if (configuration.discordToken == null) {
            LoggerFactory.getLogger(DiscordAPIController.class).info("Discord credentials not provided. Discord will not be connected");
            return;
        }

        try {
            jda = JDABuilder.createLight(configuration.discordToken).addEventListeners(this).build();
        } catch (Exception e) {
            LoggerFactory.getLogger(DiscordAPIController.class).info("Error connecting to Discord");
        }
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        super.onReady(event);
        LoggerFactory.getLogger(DiscordAPIController.class).info("Connected to Discord!");
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        if (event.getAuthor().isBot()) {
            return;
        }

        ChatEvent chatEvent = new ChatEvent(MessageOrigin.Discord, null, event.getChannel().getId(), event.getMessage().getContentDisplay());
        eventManager.dispatchEvent(chatEvent);
    }

    public void sendMessage(String channelId, String message) {
        jda.getTextChannelById(channelId).sendMessage(message).queue();
    }

    public JDA getJDA() {
        return jda;
    }
}
