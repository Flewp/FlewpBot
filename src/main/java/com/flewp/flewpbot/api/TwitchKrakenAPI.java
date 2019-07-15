package com.flewp.flewpbot.api;

import com.flewp.flewpbot.model.api.ChatRoomsResponse;
import com.flewp.flewpbot.model.api.GetStreamResponse;
import com.flewp.flewpbot.model.kraken.KrakenChannel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TwitchKrakenAPI {
    @GET("chat/{channelID}/rooms")
    Call<ChatRoomsResponse> chatRooms(@Path("channelID") String channelID);

    @GET("channels/{userID}")
    Call<KrakenChannel> channel(@Path("userID") String userID);

    @GET("streams/")
    Call<GetStreamResponse> stream(@Query("channel") String channelID);
}
