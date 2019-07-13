package com.flewp.flewpbot.api;

import com.flewp.flewpbot.model.api.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JamisphereAPI {
    @POST("request/behalf")
    Call<JamisphereRequestResponse> request(@Body JamisphereRequestBody body);

    @POST("request/play")
    Call<JamisphereRequestPlayResponse> requestPlay(@Body JamisphereRequestPlayBody body);

    @POST("request/list/clear")
    Call<EmptyResponse> listClear();

    @GET("request/list")
    Call<JamisphereRequestListResponse> requestList();
}
