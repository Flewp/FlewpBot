package com.flewp.flewpbot.api;

import com.flewp.flewpbot.model.api.JamisphereRequestBody;
import com.flewp.flewpbot.model.api.JamisphereRequestResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface JamisphereAPI {
    @POST("request")
    Call<JamisphereRequestResponse> request(@Body JamisphereRequestBody body);
}
