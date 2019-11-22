package com.flewp.flewpbot.model.api;

import com.flewp.flewpbot.model.twitch.TwitchFollow;
import com.flewp.flewpbot.model.twitch.TwitchPagination;

import java.util.List;

public class GetFollowsResponse {
    public Integer total;
    public List<TwitchFollow> data;
    public TwitchPagination pagination;
}
