package com.cleanarchitecture.sl.sl;

import android.support.annotation.NonNull;

import com.cleanarchitecture.sl.data.Result;
import com.cleanarchitecture.sl.delegate.impl.RequestDelegate;
import com.cleanarchitecture.sl.request.AbsRequest;
import com.cleanarchitecture.sl.request.Request;
import com.cleanarchitecture.sl.request.ResponseListener;
import com.cleanarchitecture.sl.task.IExecutor;
import com.cleanarchitecture.sl.task.RequestThreadPoolExecutor;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Модуль выполнения запросов
 */
public class RequestModule extends AbsModule implements IRequestModule {

    public static final String NAME = RequestModule.class.getName();

    private RequestThreadPoolExecutor mSequentiallyThreadPoolExecutor;
    private static long KEEP_ALIVE_TIME = 10; // 10 мин
    private RequestDelegate mRequestDelegate = new RequestDelegate();

    public RequestModule() {
        final BlockingQueue serQueue = new LinkedBlockingQueue<AbsRequest>();
        mSequentiallyThreadPoolExecutor = new RequestThreadPoolExecutor(1, 1, KEEP_ALIVE_TIME, TimeUnit.MINUTES, serQueue);
    }

    @Override
    public void request(final Object sender, final Request request) {
        if (request != null && validate()) {
            mRequestDelegate.processing(sender, request);
        }
    }

    @Override
    public void requestSequentially(final Object sender, final Request request) {
        if (request != null && validate()) {
            mSequentiallyThreadPoolExecutor.addRequest(request);
        }
    }

    @Override
    public void onUnRegisterModule() {
        for (IExecutor executor : mRequestDelegate.getAll()) {
            executor.clear();
            executor.shutdown();
        }
        mSequentiallyThreadPoolExecutor.clear();
        mSequentiallyThreadPoolExecutor.shutdown();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Result<Boolean> validateExt() {
        return new Result<>(super.validateExt().getData() && mRequestDelegate != null
                && mSequentiallyThreadPoolExecutor != null);
    }

    @Override
    public void cancelRequests(Object sender, ResponseListener listener) {
        final IExecutor executor = mRequestDelegate.get(sender);
        if (executor != null) {
            executor.cancelRequests(listener);
        }
        mSequentiallyThreadPoolExecutor.cancelRequests(listener);
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (IRequestModule.class.isInstance(o)) ? 0 : 1;
    }
}
