package com.cleanarchitecture.sl.repository;

import okhttp3.OkHttpClient;

/**
 * Created by Shishkin on 22.12.2017.
 */

public interface INetProvider<T> extends Provider {

    OkHttpClient getOkHttpClient();

    Class<T> getApiClass();

    String getBaseUrl();
}
