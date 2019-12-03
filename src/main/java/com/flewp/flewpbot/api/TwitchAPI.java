package com.flewp.flewpbot.api;

import com.flewp.flewpbot.model.api.GetFollowsResponse;
import com.flewp.flewpbot.model.api.GetStreamsResponse;
import com.flewp.flewpbot.model.api.GetSubscriptionsResponse;
import com.flewp.flewpbot.model.api.GetUsersResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface TwitchAPI {
    @GET("users")
    Call<GetUsersResponse> getUsers(@Query("id") List<String> id, @Query("login") List<String> login);

    @GET("users/follows")
    Call<GetFollowsResponse> getFollows(@Query("from_id") String from_id, @Query("to_id") String to_id);

    @GET("subscriptions")
    Call<GetSubscriptionsResponse> getSubscriptions(@Query("broadcaster_id") String broadcaster_id, @Query("after") String cursor);

    @GET("streams")
    Call<GetStreamsResponse> getStreams(@Query("user_login") String user_login);
}
