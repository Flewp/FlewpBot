package com.flewp.flewpbot.model.api;

import com.flewp.flewpbot.model.helix.HelixPagination;
import com.flewp.flewpbot.model.helix.HelixSubscription;

import java.util.List;

public class GetSubscriptionsResponse {
    public List<HelixSubscription> data;
    public HelixPagination pagination;
}
