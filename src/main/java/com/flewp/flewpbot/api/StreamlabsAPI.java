package com.flewp.flewpbot.api;

import com.flewp.flewpbot.model.api.GetDonationsResponse;
import com.flewp.flewpbot.model.api.RefreshStreamlabsTokenResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface StreamlabsAPI {

    @GET("donations")
    Call<GetDonationsResponse> getDonations(@Query("access_tokeN") String accessToken, @Query("after") Integer after,
                                            @Query("limit") Integer limit);
}
