package com.flewp.flewpbot.api;

import com.flewp.flewpbot.model.kraken.KrakenChannel;
import com.flewp.flewpbot.model.api.ChatRoomsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TwitchKrakenAPI {
    @GET("chat/{channelID}/rooms")
    Call<ChatRoomsResponse> chatRooms(@Path("channelID") String channelID);

    @GET("channels/{userID}")
    Call<KrakenChannel> channel(@Path("userID") String userID);
}
