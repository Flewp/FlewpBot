package com.flewp.flewpbot.api;

import com.flewp.flewpbot.model.Channel;
import com.flewp.flewpbot.model.ChatRoomsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TwitchKrakenAPI {
    @GET("chat/{channelID}/rooms")
    Call<ChatRoomsResponse> chatRooms(@Path("channelID") String channelID);

    @GET("channel")
    Call<Channel> channel();
}
