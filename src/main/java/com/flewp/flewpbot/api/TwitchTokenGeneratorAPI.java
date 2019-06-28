package com.flewp.flewpbot.api;

import com.flewp.flewpbot.model.api.RefreshTokenResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TwitchTokenGeneratorAPI {
    @GET("api/refresh/{refreshToken}")
    Call<RefreshTokenResponse> refreshToken(@Path("refreshToken") String refreshToken);
}
