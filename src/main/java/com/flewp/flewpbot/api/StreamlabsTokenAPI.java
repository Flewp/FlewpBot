package com.flewp.flewpbot.api;

import com.flewp.flewpbot.model.api.RefreshStreamlabsTokenResponse;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface StreamlabsTokenAPI {
    @POST("token")
    Call<RefreshStreamlabsTokenResponse> refreshToken(@Query("grant_type") String grantType, @Query("client_id") String clientId,
                                                      @Query("client_secret") String clientSecret, @Query("redirect_url") String redirectUrl);
}
