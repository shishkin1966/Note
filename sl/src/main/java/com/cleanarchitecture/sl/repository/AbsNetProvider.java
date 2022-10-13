package com.cleanarchitecture.sl.repository;

import android.content.Context;
import androidx.annotation.NonNull;

import com.cleanarchitecture.sl.data.Result;
import com.cleanarchitecture.sl.request.Request;
import com.cleanarchitecture.sl.request.ResponseListener;
import com.cleanarchitecture.sl.sl.ApplicationModule;
import com.cleanarchitecture.sl.sl.SLUtil;


import java.util.concurrent.TimeUnit;


import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class AbsNetProvider<T> extends AbsProvider implements INetProvider<T> {
    private static long CONNECT_TIMEOUT = 30; // 30 sec
    private static long READ_TIMEOUT = 10; // 10 min
    private Retrofit mRetrofit;
    private T mApi;

    public AbsNetProvider() {
        final Context context = ApplicationModule.getInstance();
        if (context == null) {
            return;
        }

        mRetrofit = new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mApi = mRetrofit.create(getApiClass());
    }

    public T getApi() {
        return mApi;
    }

    public abstract Class<T> getApiClass();

    public abstract String getBaseUrl();

    public OkHttpClient getOkHttpClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.MINUTES)
                .retryOnConnectionFailure(false);
        return builder.build();
    }

    @Override
    public void request(final Request request) {
        if (request != null && validate()) {
            SLUtil.getRequestModule().request(this, request);
        }
    }

    @Override
    public Result<Boolean> validateExt() {
        return new Result<>(mRetrofit != null && mApi != null);
    }

    @Override
    public boolean isPersistent() {
        return false;
    }

    @Override
    public void onUnRegisterModule() {
    }

    public void cancelRequests(ResponseListener listener) {
        SLUtil.getRequestModule().cancelRequests(this, listener);
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (INetProvider.class.isInstance(o)) ? 0 : 1;
    }

}


