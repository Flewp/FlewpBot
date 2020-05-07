package com.flewp.flewpbot.api;

import com.flewp.flewpbot.model.api.TwitchTokenResponse;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TwitchAuthAPI {
    @POST("oauth2/token")
    Call<TwitchTokenResponse> token(@Query("client_id") String clientId, @Query("client_secret") String clientSecret,
                                    @Query("grant_type") String grantType, @Query("refresh_token") String refreshToken);
}
