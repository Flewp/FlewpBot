package com.flewp.flewpbot;

import com.flewp.flewpbot.api.JamisphereAPI;
import com.flewp.flewpbot.api.StreamlabsAPI;
import com.flewp.flewpbot.api.TwitchAPI;
import com.flewp.flewpbot.api.controller.DiscordAPIController;
import com.flewp.flewpbot.api.controller.StreamlabsAPIController;
import com.flewp.flewpbot.api.controller.TwitchAPIController;
import com.flewp.flewpbot.api.controller.TwitchPubSubController;
import com.flewp.flewpbot.model.api.GetFollowsResponse;
import com.flewp.flewpbot.model.events.jamisphere.*;
import com.flewp.flewpbot.model.events.twitch.*;
import com.flewp.flewpbot.pusher.PusherManager;
import com.pusher.client.Pusher;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.LoggerFactory;
import retrofit2.Response;

import javax.inject.Inject;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class FlewpBot {
    private Configuration configuration;
    private List<FlewpBotListener> listenerList = new ArrayList<>();
    private WebSocketClient client;
    private List<FlewpBotMIDIReceiver> receiverList = new ArrayList<>();


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
    DiscordAPIController discordAPIController;

    @Inject
    PusherManager pusherManager;

    @Inject
    TwitchPubSubController twitchPubSubController;

    public FlewpBot() {
        configuration = Configuration.getInstance();

        DaggerFlewpBotComponent.builder()
                .flewpBotModule(new FlewpBotModule(configuration))
                .build().inject(this);

        eventManager.onEvent(ChatEvent.class).subscribe(this::onChatMessage);
        eventManager.onEvent(BitEvent.class).subscribe(this::onCheer);
        eventManager.onEvent(SubscribeEvent.class).subscribe(this::onSubscribe);
        eventManager.onEvent(NewDonationEvent.class).subscribe(this::onNewDonation);
        eventManager.onEvent(RequestAddedEvent.class).subscribe(this::onRequestAdded);
        eventManager.onEvent(RequestLikedEvent.class).subscribe(this::onRequestLiked);
        eventManager.onEvent(RequestListClearedEvent.class).subscribe(this::onRequestListCleared);
        eventManager.onEvent(RequestPlayedEvent.class).subscribe(this::onRequestPlayed);
        eventManager.onEvent(RequestFinishedEvent.class).subscribe(this::onRequestFinished);
        eventManager.onEvent(RequestRemovedEvent.class).subscribe(this::onRequestRemoved);
        eventManager.onEvent(RequestUnlikedEvent.class).subscribe(this::onRequestUnliked);
        eventManager.onEvent(RequestUpgradedEvent.class).subscribe(this::onRequestUpgraded);
        eventManager.onEvent(RequestDowngradedEvent.class).subscribe(this::onRequestDowngraded);
        eventManager.onEvent(CommandsUpdatedEvent.class).subscribe(this::onCommandsUpdated);
        eventManager.onEvent(PointsRedeemedEvent.class).subscribe(this::onPointsRedeemed);
        eventManager.onEvent(EventStartedEvent.class).subscribe(this::onEventStarted);
        eventManager.onEvent(EventEnteredEvent.class).subscribe(this::onEventEntered);
        eventManager.onEvent(EventFinishedEvent.class).subscribe(this::onEventFinished);
    }

    synchronized public void start() {
        if (configuration.enableIrc) {
            twitchAPIController.startChatBot();
        }

        if (configuration.enablePubSub) {
            twitchPubSubController.startTwitchPubSub();
        }

        if (configuration.isStreamlabsConnectable()) {
            streamlabsAPIController.startQueryingDonations();
        }

        if (configuration.enableMidi) {
            connectWebSocket();
        }

        discordAPIController.start();

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

    synchronized private void onRequestFinished(RequestFinishedEvent requestFinishedEvent) {
        LoggerFactory.getLogger(FlewpBot.class).info(requestFinishedEvent.toString());
        listenerList.forEach(listener -> listener.onRequestFinished(requestFinishedEvent));
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

    synchronized private void onRequestDowngraded(RequestDowngradedEvent requestDowngradedEvent) {
        LoggerFactory.getLogger(FlewpBot.class).info(requestDowngradedEvent.toString());
        listenerList.forEach(listener -> listener.onRequestDowngraded(requestDowngradedEvent));
    }

    synchronized private void onCommandsUpdated(CommandsUpdatedEvent commandsUpdatedEvent) {
        LoggerFactory.getLogger(FlewpBot.class).info(commandsUpdatedEvent.toString());
        listenerList.forEach(listener -> listener.onCommandsUpdated(commandsUpdatedEvent));
    }

    synchronized private void onPointsRedeemed(PointsRedeemedEvent pointsRedeemedEvent) {
        LoggerFactory.getLogger(FlewpBot.class).info(pointsRedeemedEvent.toString());
        listenerList.forEach(listener -> listener.onPointsRedeemed(pointsRedeemedEvent));
    }

    synchronized private void onEventStarted(EventStartedEvent eventStartedEvent) {
        LoggerFactory.getLogger(FlewpBot.class).info(eventStartedEvent.toString());
        listenerList.forEach(listener -> listener.onEventStarted(eventStartedEvent));
    }

    synchronized private void onEventEntered(EventEnteredEvent eventEnteredEvent) {
        LoggerFactory.getLogger(FlewpBot.class).info(eventEnteredEvent.toString());
        listenerList.forEach(listener -> listener.onEventEntered(eventEnteredEvent));
    }

    synchronized private void onEventFinished(EventFinishedEvent eventFinishedEvent) {
        LoggerFactory.getLogger(FlewpBot.class).info(eventFinishedEvent.toString());
        listenerList.forEach(listener -> listener.onEventFinished(eventFinishedEvent));
    }

    private void connectWebSocket() {
        if (client != null && (!client.isClosing() || !client.isClosed() || !client.isFlushAndClose())) {
            client.close();
            client = null;
        }

        WebSocketClient client = new WebSocketClient(URI.create("ws://[::1]:12321/midi")) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                LoggerFactory.getLogger(FlewpBot.class).info("MIDI Websocket opened!");
            }

            @Override
            public void onMessage(String message) {
                if (message == null || message.isEmpty()) {
                    return;
                }

                String[] split = message.split("\\$");
                if (split.length != 3) {
                    return;
                }

                ShortMessage midi;
                int timestamp;

                try {
                    int parse = Integer.parseInt(split[1]);

                    byte[] bytes = new byte[]{
                            (byte) parse, // Command / Channel
                            (byte) (parse >> 8), // Data byte 1
                            (byte) (parse >> 16), // Data byte 2
                    };

                    midi = new ShortMessage(bytes[0] & 0xF0, bytes[0] & 0x0F, bytes[1], bytes[2]);
                    timestamp = Integer.parseInt(split[2]);
                } catch (Exception e) {
                    return;
                }

                sendMIDIMessage(split[0], midi, timestamp);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                LoggerFactory.getLogger(FlewpBot.class).info("MIDI Websocket closed. " + reason);
            }

            @Override
            public void onError(Exception ex) {
                LoggerFactory.getLogger(FlewpBot.class).info("MIDI Websocket error. Going to attempt reconnect." + ex.toString());
                new Thread(FlewpBot.this::connectWebSocket).start();
            }
        };
        client.connect();
    }

    synchronized public void sendMessage(String channel, String message) {
        if (configuration.enableIrc) {
            twitchAPIController.getIrcClient().sendMessage(channel, message);
        } else {
            throw new IllegalStateException("You must set enableIrc to true in order to use this function");
        }
    }

    synchronized public void sendWhisper(String userName, String message) {
        if (configuration.enableIrc) {
            twitchAPIController.getIrcClient().sendMessage("#jtv", "/w " + userName + ' ' + message);
        }
    }

    synchronized public void sendTwitchChatMessage(String message) {
        if (configuration.enableIrc) {
            twitchAPIController.getIrcClient().sendMessage("#" + configuration.twitchStreamerName, message);
        } else {
            throw new IllegalStateException("You must set enableIrc to true in order to use this function");
        }
    }

    synchronized public void sendMIDIMessage(String deviceName, MidiMessage message, long timestamp) {
        receiverList.stream().filter(receiver ->
                receiver.isMatchExact() ? receiver.getName().equalsIgnoreCase(deviceName) :
                        deviceName.toLowerCase().contains(receiver.getName().toLowerCase())
        ).forEach(receiver -> {
            receiver.getMidiMessageCallback().onMIDIMessage(deviceName, message, timestamp);
        });
    }

    synchronized public void addMIDIReceiver(FlewpBotMIDIReceiver flewpBotMIDIReceiver) {
        if (flewpBotMIDIReceiver == null) {
            return;
        }

        receiverList.add(flewpBotMIDIReceiver);
    }

    synchronized public void removeMIDIReceiver(FlewpBotMIDIReceiver flewpBotMIDIReceiver) {
        if (flewpBotMIDIReceiver == null) {
            return;
        }

        receiverList.remove(flewpBotMIDIReceiver);
    }

    synchronized public void sendDiscordMessage(String channelId, String message) {
        if (configuration.discordToken != null) {
            discordAPIController.sendMessage(channelId, message);
        } else {
            throw new IllegalStateException("You must set discordToken in order to use this function");
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

    public PusherManager getPusherManager() {
        return pusherManager;
    }

    public Pusher getPusher() {
        return getPusherManager().getPusher();
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
