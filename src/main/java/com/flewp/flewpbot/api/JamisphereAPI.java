package com.flewp.flewpbot.api;

import com.flewp.flewpbot.model.api.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JamisphereAPI {
    @POST("request/behalf")
    Call<JamisphereRequestResponse> requestBehalf(@Body JamisphereRequestBehalfBody body);

    @POST("request/upgrade/behalf")
    Call<JamisphereRequestResponse> requestUpgradeBehalf(@Body JamisphereRequestUpgradeBehalfBody body);

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
    Call<JamisphereGuessingGameAnswerResponse> guessingGameAnswer(@Body JamisphereGuessingGameAnswerBody body);

    @POST("guessinggame/start")
    Call<JamisphereGuessingGameStartResponse> guessingGameStart(@Body JamisphereGuessingGameStartBody body);

    @POST("guessinggame/guess/behalf")
    Call<JamisphereBaseResponse> guessingGameGuessBehalf(@Body JamisphereGuessingGameGuessBehalfBody body);

    @POST("drums")
    Call<JamisphereBaseResponse> drums(@Body JamisphereDrumsBody body);

    @POST("choicegame/start")
    Call<JamisphereChoiceGameResponse> choiceGameStart(@Body JamisphereChoiceGameStartBody body);

    @POST("choicegame/answer")
    Call<JamisphereChoiceGameResponse> choiceGameAnswer(@Body JamisphereChoiceGameAnswerBody body);

    @POST("choicegame/choice/behalf")
    Call<JamisphereBaseResponse> choiceGameChoiceBehalf(@Body JamisphereChoiceGameChoiceBehalfBody body);

    @POST("commands")
    Call<JamisphereBaseResponse> commands(@Body JamisphereCommandsBody body);

    @GET("commands")
    Call<JamisphereCommandsResponse> commands();

    @POST("pusher")
    Call<JamisphereBaseResponse> pusher(@Body JamispherePusherBody body);
}
