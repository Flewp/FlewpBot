package com.flewp.flewpbot.model.events.jamisphere;

import com.flewp.flewpbot.model.events.BaseEvent;
import com.flewp.flewpbot.model.events.twitch.pubsub.RedemptionData;

public class PointsRedeemedEvent extends BaseEvent {
    public RedemptionData data;

    public PointsRedeemedEvent(RedemptionData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PointsRedeemedEvent{" +
                "data=" + data +
                '}';
    }
}
