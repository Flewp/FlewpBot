package com.flewp.flewpbot.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TwitchTokenGeneratorAPI {
    @GET("api/refresh/{refreshToken}")
    Call<RefreshTokenResponse> refreshToken(@Path("refreshToken") String refreshToken);
}
