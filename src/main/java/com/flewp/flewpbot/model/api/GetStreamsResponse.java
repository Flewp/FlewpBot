package com.flewp.flewpbot.model.api;

import com.flewp.flewpbot.model.twitch.TwitchPagination;
import com.flewp.flewpbot.model.twitch.TwitchStream;

import java.util.List;

public class GetStreamsResponse {
    public List<TwitchStream> data;
    public TwitchPagination cursor;
}
