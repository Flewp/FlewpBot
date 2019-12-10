package com.flewp.flewpbot.api;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.annotation.Nonnull;

public class RetrofitEmptyCallback<T> implements Callback<T> {

    @Override
    public void onResponse(@Nonnull Call<T> call, @Nonnull Response<T> response) {

    }

    @Override
    public void onFailure(@Nonnull Call<T> call, @Nonnull Throwable t) {

    }
}
