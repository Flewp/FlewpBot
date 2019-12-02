package com.flewp.flewpbot;

import com.flewp.flewpbot.api.JamisphereAPI;
import com.flewp.flewpbot.api.StreamlabsAPI;
import com.flewp.flewpbot.api.TwitchAPI;
import com.flewp.flewpbot.api.controller.StreamlabsAPIController;
import com.flewp.flewpbot.api.controller.TwitchAPIController;
import com.flewp.flewpbot.model.api.GetFollowsResponse;
import com.flewp.flewpbot.model.events.jamisphere.*;
import com.flewp.flewpbot.model.events.twitch.*;
import com.flewp.flewpbot.pusher.PusherManager;
import com.github.philippheuer.events4j.EventManager;
import org.slf4j.LoggerFactory;
import retrofit2.Response;
import sun.rmi.runtime.Log;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class FlewpBot {
    private Configuration configuration;
    private List<FlewpBotListener> listenerList = new ArrayList<>();

    @Inject
    EventManager eventManager;

    @Inject
    TwitchAPI twitchAPI;

    @Inject
    StreamlabsAPI streamlabsAPI;

    @Inject
    JamisphereAPI jamisphereAPI;

    @Inject
    TwitchAPIController twitchAPIController;

    @Inject
    StreamlabsAPIController streamlabsAPIController;

    @Inject
    PusherManager pusherManager;

    public FlewpBot() {
        configuration = Configuration.getInstance();

        DaggerFlewpBotComponent.builder()
                .flewpBotModule(new FlewpBotModule(configuration))
                .build().inject(this);

        eventManager.onEvent(WhisperEvent.class).subscribe(this::onWhisperMessage);
        eventManager.onEvent(ChatEvent.class).subscribe(this::onChatMessage);
        eventManager.onEvent(BitEvent.class).subscribe(this::onCheer);
        eventManager.onEvent(SubscribeEvent.class).subscribe(this::onSubscribe);
        eventManager.onEvent(NewDonationEvent.class).subscribe(this::onNewDonation);
        eventManager.onEvent(GuessingGameAnsweredEvent.class).subscribe(this::onGuessingGameAnswered);
        eventManager.onEvent(GuessingGameStartedEvent.class).subscribe(this::onGuessingGameStarted);
        eventManager.onEvent(RequestAddedEvent.class).subscribe(this::onRequestAdded);
        eventManager.onEvent(RequestLikedEvent.class).subscribe(this::onRequestLiked);
        eventManager.onEvent(RequestListClearedEvent.class).subscribe(this::onRequestListCleared);
        eventManager.onEvent(RequestPlayedEvent.class).subscribe(this::onRequestPlayed);
        eventManager.onEvent(RequestRemovedEvent.class).subscribe(this::onRequestRemoved);
        eventManager.onEvent(RequestUnlikedEvent.class).subscribe(this::onRequestUnliked);
        eventManager.onEvent(RequestUpgradedEvent.class).subscribe(this::onRequestUpgraded);
        eventManager.onEvent(ChoiceGameStartedEvent.class).subscribe(this::onChoiceGameStarted);
        eventManager.onEvent(ChoiceGameChoiceEnteredEvent.class).subscribe(this::onChoiceGameChoiceEntered);
        eventManager.onEvent(ChoiceGameAnsweredEvent.class).subscribe(this::onChoiceGameAnswered);
        eventManager.onEvent(CommandsUpdatedEvent.class).subscribe(this::onCommandsUpdated);
    }

    synchronized public void start() {
        if (configuration.enableIrc) {
            twitchAPIController.startChatBot();
        }
        streamlabsAPIController.startQueryingDonations();
        pusherManager.connect();
    }

    synchronized public void addListener(FlewpBotListener listener) {
        if (!listenerList.contains(listener)) {
            listenerList.add(listener);
        }
    }

    synchronized public void removeListener(FlewpBotListener listener) {
        listenerList.remove(listener);
    }

    synchronized private void onNewDonation(NewDonationEvent newDonationEvent) {
        LoggerFactory.getLogger(FlewpBot.class).info(newDonationEvent.toString());
        listenerList.forEach(listener -> listener.onNewDonation(newDonationEvent));
    }

    synchronized private void onSubscribe(SubscribeEvent subscribeEvent) {
        LoggerFactory.getLogger(FlewpBot.class).info(subscribeEvent.toString());
        listenerList.forEach(listener -> listener.onSubscribe(subscribeEvent));
    }

    synchronized private void onCheer(BitEvent bitEvent) {
        LoggerFactory.getLogger(FlewpBot.class).info(bitEvent.toString());
        listenerList.forEach(listener -> listener.onCheer(bitEvent));
    }

    synchronized private void onChatMessage(ChatEvent chatEvent) {
        LoggerFactory.getLogger(FlewpBot.class).info(chatEvent.toString());
        listenerList.forEach(listener -> listener.onChatMessage(chatEvent));
    }

    synchronized private void onWhisperMessage(WhisperEvent whisperEvent) {
        LoggerFactory.getLogger(FlewpBot.class).info(whisperEvent.toString());
        listenerList.forEach(listener -> listener.onWhisperMessage(whisperEvent));
    }

    synchronized private void onGuessingGameAnswered(GuessingGameAnsweredEvent guessingGameAnsweredEvent) {
        LoggerFactory.getLogger(FlewpBot.class).info(guessingGameAnsweredEvent.toString());
        listenerList.forEach(listener -> listener.onGuessingGameAnswered(guessingGameAnsweredEvent));
    }

    synchronized private void onGuessingGameStarted(GuessingGameStartedEvent guessingGameStartedEvent) {
        LoggerFactory.getLogger(FlewpBot.class).info(guessingGameStartedEvent.toString());
        listenerList.forEach(listener -> listener.onGuessingGameStarted(guessingGameStartedEvent));
    }

    synchronized private void onRequestAdded(RequestAddedEvent requestAddedEvent) {
        LoggerFactory.getLogger(FlewpBot.class).info(requestAddedEvent.toString());
        listenerList.forEach(listener -> listener.onRequestAdded(requestAddedEvent));
    }

    synchronized private void onRequestLiked(RequestLikedEvent requestLikedEvent) {
        LoggerFactory.getLogger(FlewpBot.class).info(requestLikedEvent.toString());
        listenerList.forEach(listener -> listener.onRequestLiked(requestLikedEvent));
    }

    synchronized private void onRequestListCleared(RequestListClearedEvent requestListClearedEvent) {
        LoggerFactory.getLogger(FlewpBot.class).info(requestListClearedEvent.toString());
        listenerList.forEach(listener -> listener.onRequestListCleared(requestListClearedEvent));
    }

    synchronized private void onRequestPlayed(RequestPlayedEvent requestPlayedEvent) {
        LoggerFactory.getLogger(FlewpBot.class).info(requestPlayedEvent.toString());
        listenerList.forEach(listener -> listener.onRequestPlayed(requestPlayedEvent));
    }

    synchronized private void onRequestRemoved(RequestRemovedEvent requestRemovedEvent) {
        LoggerFactory.getLogger(FlewpBot.class).info(requestRemovedEvent.toString());
        listenerList.forEach(listener -> listener.onRequestRemoved(requestRemovedEvent));
    }

    synchronized private void onRequestUnliked(RequestUnlikedEvent requestUnlikedEvent) {
        LoggerFactory.getLogger(FlewpBot.class).info(requestUnlikedEvent.toString());
        listenerList.forEach(listener -> listener.onRequestUnliked(requestUnlikedEvent));
    }

    synchronized private void onRequestUpgraded(RequestUpgradedEvent requestUpgradedEvent) {
        LoggerFactory.getLogger(FlewpBot.class).info(requestUpgradedEvent.toString());
        listenerList.forEach(listener -> listener.onRequestUpgraded(requestUpgradedEvent));
    }

    synchronized private void onChoiceGameStarted(ChoiceGameStartedEvent choiceGameStartedEvent) {
        LoggerFactory.getLogger(FlewpBot.class).info(choiceGameStartedEvent.toString());
        listenerList.forEach(listener -> listener.onChoiceGameStarted(choiceGameStartedEvent));
    }

    synchronized private void onChoiceGameChoiceEntered(ChoiceGameChoiceEnteredEvent choiceGameChoiceEnteredEvent) {
        LoggerFactory.getLogger(FlewpBot.class).info(choiceGameChoiceEnteredEvent.toString());
        listenerList.forEach(listener -> listener.onChoiceGameChoiceEntered(choiceGameChoiceEnteredEvent));
    }

    synchronized private void onChoiceGameAnswered(ChoiceGameAnsweredEvent choiceGameAnsweredEvent) {
        LoggerFactory.getLogger(FlewpBot.class).info(choiceGameAnsweredEvent.toString());
        listenerList.forEach(listener -> listener.onChoiceGameAnswered(choiceGameAnsweredEvent));
    }

    synchronized private void onCommandsUpdated(CommandsUpdatedEvent commandsUpdatedEvent) {
        LoggerFactory.getLogger(FlewpBot.class).info(commandsUpdatedEvent.toString());
        listenerList.forEach(listener -> listener.onCommandsUpdated(commandsUpdatedEvent));
    }

    synchronized public void sendMessage(String channel, String message) {
        if (twitchAPIController != null) {
            twitchAPIController.getIrcClient().sendMessage(channel, message);
        } else {
            throw new IllegalStateException("You must set enableIrc to true in order to use this function");
        }
    }

    synchronized public void sendWhisper(String userName, String message) {
        if (twitchAPIController != null) {
            twitchAPIController.getIrcClient().sendMessage("#jtv", "/w " + userName + ' ' + message);
        }
    }

    synchronized public void sendTwitchChatMessage(String message) {
        if (twitchAPIController != null) {
            twitchAPIController.getIrcClient().sendMessage("#" + configuration.twitchStreamerName, message);
        } else {
            throw new IllegalStateException("You must set enableIrc to true in order to use this function");
        }
    }

    public TwitchAPI getTwitchAPI() {
        return twitchAPI;
    }

    public StreamlabsAPI getStreamlabsAPI() {
        return streamlabsAPI;
    }

    public JamisphereAPI getJamisphereAPI() {
        return jamisphereAPI;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public String getStreamerUserId() {
        if (twitchAPIController != null) {
            return twitchAPIController.getStreamerUserId();
        } else {
            throw new IllegalStateException("You must set enableIrc to true in order to use this function");
        }
    }

    public String getStreamerChannelId() {
        if (twitchAPIController != null) {
            return twitchAPIController.getStreamerChannelId();
        } else {
            throw new IllegalStateException("You must set enableIrc to true in order to use this function");
        }
    }

    public String getTwitchUserPermission(EventUser user) {
        if (user == null) {
            return "unknown";
        }

        if (user.getPermissions() != null) {
            if (user.getPermissions().contains(EventUser.Permission.Broadcaster)) {
                return "streamer";
            } else if (user.getPermissions().contains(EventUser.Permission.Moderator)) {
                return "moderator";
            } else if (user.getPermissions().contains(EventUser.Permission.Subscriber)) {
                return "subscriber";
            }
        }

        if (user.getId() != null && twitchAPIController != null) {
            try {
                Response<GetFollowsResponse> response = twitchAPI.getFollows(user.getId(),
                        twitchAPIController.getStreamerUserId()).execute();

                if (response.isSuccessful() && response.body() != null
                        && response.body().data != null && !response.body().data.isEmpty()) {
                    return "follower";
                } else {
                    return "regular";
                }
            } catch (Exception e) {
                // Fail silently
            }
        }

        return "unknown";
    }
}
