package com.flewp.flewpbot.api;

import com.flewp.flewpbot.model.events.twitch.pubsub.PubsubEvent;
import com.tinder.scarlet.Stream;
import com.tinder.scarlet.WebSocket;
import com.tinder.scarlet.ws.Receive;
import com.tinder.scarlet.ws.Send;

public interface TwitchPubsubAPI {

    @Receive
    Stream<WebSocket.Event> observeWebSocketEvents();

    @Receive
    Stream<PubsubEvent> observePubsubEvents();

    @Send
    void sendEvent(PubsubEvent event);
}
