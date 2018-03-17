package com.cleanarchitecture.sl.request;

import com.cleanarchitecture.common.net.Connectivity;
import com.cleanarchitecture.sl.data.ErrorCode;
import com.cleanarchitecture.sl.data.Result;
import com.cleanarchitecture.sl.mail.impl.ResultMail;
import com.cleanarchitecture.sl.sl.ApplicationModule;
import com.cleanarchitecture.sl.sl.SLUtil;
import com.cleanarchitecture.sl.sl.Subscriber;


import bolts.Continuation;
import bolts.Task;
import bolts.TaskCompletionSource;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Shishkin on 04.12.2017.
 */

public abstract class AbsNetResultMailRequest<T> extends AbsResultMailRequest implements ResultMailRequest {

    public AbsNetResultMailRequest(final String listener) {
        super(listener);
    }

    @Override
    public void run() {
        if (!validate()) return;

        getFromNet().continueWith((Continuation<Result<T>, Void>) task -> {
            if (validate() && task.getResult() != null) {
                SLUtil.addMail(new ResultMail(getListener(), task.getResult().setOrder(Result.LAST).setName(getName())).setName(getName()));
            }
            return null;
        }, Task.UI_THREAD_EXECUTOR);
    }

    public abstract Call<T> getData();

    public Task<Result<T>> getFromNet() {
        final TaskCompletionSource<Result<T>> newTask = new TaskCompletionSource<>();

        if (validate()) {
            if (Connectivity.isNetworkConnected(ApplicationModule.getInstance())) {
                getData().enqueue(new BaseCallback<T>(this) {
                    @Override
                    public void proceedResponse(final Response response, final Subscriber request) {
                        super.proceedResponse(response, request);

                        newTask.setResult(getResult());
                    }

                    @Override
                    public void proceedError(final Throwable t, final Subscriber request) {
                        super.proceedError(t, request);

                        newTask.setResult(getResult());
                    }
                });
            } else {
                newTask.setResult(new Result().setError(getName(), ErrorCode.ERROR_NOT_NETWORK).setOrder(Result.LAST).setName(getName()));
            }
        } else {
            newTask.setResult(new Result<>());
        }
        return newTask.getTask();
    }

    @Override
    public boolean validate() {
        return (super.validate() && Connectivity.isNetworkConnected(ApplicationModule.getInstance()));
    }

}
