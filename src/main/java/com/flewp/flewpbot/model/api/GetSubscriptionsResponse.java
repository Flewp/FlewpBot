package com.flewp.flewpbot.model.api;

import com.flewp.flewpbot.model.twitch.TwitchPagination;
import com.flewp.flewpbot.model.twitch.TwitchSubscription;

import java.util.List;

public class GetSubscriptionsResponse {
    public List<TwitchSubscription> data;
    public TwitchPagination pagination;
}
