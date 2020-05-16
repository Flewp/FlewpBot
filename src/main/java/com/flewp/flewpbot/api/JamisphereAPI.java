package com.flewp.flewpbot.api;

import com.flewp.flewpbot.model.api.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface JamisphereAPI {
    @GET("request")
    Call<JamisphereRequestResponse> request(@Query("userName") String userName);

    @POST("request/behalf")
    Call<JamisphereRequestResponse> requestBehalf(@Body JamisphereRequestBehalfBody body);

    @POST("request/upgrade/behalf")
    Call<JamisphereRequestUpgradeResponse> requestUpgradeBehalf(@Body JamisphereRequestUpgradeBehalfBody body);

    @POST("request/downgrade/behalf")
    Call<JamisphereRequestResponse> requestDowngradeBehalf(@Body JamisphereRequestUpgradeBehalfBody body);

    @POST("request/transfer/behalf")
    Call<JamisphereTransferResponse> requestTransferBehalf(@Body JamisphereRequestTransferBehalfBody body);

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

    @POST("drums")
    Call<JamisphereBaseResponse> drums(@Body JamisphereDrumsBody body);

    @POST("commands")
    Call<JamisphereBaseResponse> commands(@Body JamisphereCommandsBody body);

    @GET("commands")
    Call<JamisphereCommandsResponse> commands();

    @POST("pusher")
    Call<JamisphereBaseResponse> pusher(@Body JamispherePusherBody body);

    @GET("event/list")
    Call<JamisphereEventListResponse> eventList();

    @POST("event/start")
    Call<JamisphereEventStartResponse> eventStart(@Body JamisphereEventStartBody body);

    @POST("event/finish")
    Call<JamisphereEventFinishBody> eventFinish(@Body JamisphereEventFinishBody body);

    @POST("event/enter/behalf")
    Call<JamisphereEventEnterResponse> eventEnter(@Body JamisphereEventEnterBody body);
}
