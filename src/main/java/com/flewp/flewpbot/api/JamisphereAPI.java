package com.flewp.flewpbot.api;

import com.flewp.flewpbot.model.api.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JamisphereAPI {
    @POST("request/behalf")
    Call<JamisphereRequestResponse> requestBehalf(@Body JamisphereRequestBehalfBody body);

    @POST("request/remove/behalf")
    Call<JamisphereBaseResponse> requestRemoveBehalf(@Body JamisphereRequestRemoveBehalfBody body);

    @POST("request/play")
    Call<JamisphereRequestResponse> requestPlay(@Body JamisphereRequestPlayBody body);

    @GET("request/current")
    Call<JamisphereRequestResponse> requestCurrent();

    @POST("request/list/clear")
    Call<JamisphereBaseResponse> listClear();

    @GET("request/list")
    Call<JamisphereRequestListResponse> requestList();

    @POST("guessinggame/answer")
    Call<JamisphereBaseResponse> guessingGameAnswer(@Body JamisphereGuessingGameAnswerBody body);

    @POST("guessinggame/start")
    Call<JamisphereBaseResponse> guessingGameStart();

    @POST("guessinggame/guess/behalf")
    Call<JamisphereBaseResponse> guessingGameGuessBehalf(@Body JamisphereGuessingGameGuessBehalfBody body);

    @POST("drums")
    Call<JamisphereBaseResponse> drums(@Body JamisphereDrumsBody body);
}
