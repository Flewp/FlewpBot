package com.flewp.flewpbot.api;

import com.flewp.flewpbot.model.api.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JamisphereAPI {
    @POST("request/behalf")
    Call<EmptyResponse> requestBehalf(@Body JamisphereRequestBehalfBody body);

    @POST("request/remove/behalf")
    Call<EmptyResponse> requestRemoveBehalf(@Body JamisphereRequestRemoveBehalfBody body);

    @POST("request/play")
    Call<EmptyResponse> requestPlay(@Body JamisphereRequestPlayBody body);

    @POST("request/list/clear")
    Call<EmptyResponse> listClear();

    @GET("request/list")
    Call<EmptyResponse> requestList();

    @POST("guessinggame/answer")
    Call<EmptyResponse> guessingGameAnswer(@Body JamisphereGuessingGameAnswerBody body);

    @POST("guessinggame/start")
    Call<EmptyResponse> guessingGameStart();

    @POST("guessinggame/guess/behalf")
    Call<EmptyResponse> guessingGameGuessBehalf(@Body JamisphereGuessingGameGuessBehalfBody body);
}
