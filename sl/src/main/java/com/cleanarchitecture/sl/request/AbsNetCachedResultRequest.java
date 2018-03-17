package com.cleanarchitecture.sl.request;

import com.cleanarchitecture.common.net.Connectivity;
import com.cleanarchitecture.sl.data.Result;
import com.cleanarchitecture.sl.sl.ApplicationModule;


import bolts.Continuation;
import bolts.Task;

/**
 * Created by Shishkin on 04.12.2017.
 */

public abstract class AbsNetCachedResultRequest<T> extends AbsNetResultRequest<T> implements ResultRequest {

    public AbsNetCachedResultRequest(final ResponseListener listener) {
        super(listener);
    }

    @Override
    public void run() {
        if (!validate()) return;

        Task.call(() -> readCache(), Task.BACKGROUND_EXECUTOR)
                .continueWith(task -> {
                    if (validate() && task.getResult() != null) {
                        getListener().response(new Result(task.getResult()).setOrder(Connectivity.isNetworkConnected(ApplicationModule.getInstance()) ? 0 : Result.LAST).setName(getName()));
                    }
                    return task.getResult();
                }, Task.UI_THREAD_EXECUTOR)
                .continueWithTask(task -> getFromNet(), Task.BACKGROUND_EXECUTOR)
                .continueWith(task -> {
                    if (validate() && task.getResult() != null) {
                        getListener().response(task.getResult().setOrder(Result.LAST).setName(getName()));
                    }
                    return task.getResult();
                }, Task.UI_THREAD_EXECUTOR)
                .continueWith((Continuation<Result<T>, Void>) task -> {
                    if (validate() && task.getResult() != null && !task.getResult().hasError()) {
                        writeCache(task.getResult().getData());
                    }
                    return null;
                }, Task.BACKGROUND_EXECUTOR);
    }

    public abstract T readCache();

    public abstract void writeCache(T data);

}
