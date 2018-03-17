package com.cleanarchitecture.sl.request;

import com.cleanarchitecture.sl.data.Result;


import bolts.Continuation;
import bolts.Task;

/**
 * Created by Shishkin on 04.12.2017.
 */

public abstract class AbsDataCachedResultRequest<T> extends AbsDataResultRequest<T> implements ResultRequest {

    public AbsDataCachedResultRequest(final ResponseListener listener) {
        super(listener);
    }

    @Override
    public void run() {
        if (!validate()) return;

        Task.call(() -> readCache(), Task.BACKGROUND_EXECUTOR)
                .continueWith(task -> {
                    if (validate() && task.getResult() != null) {
                        getListener().response(new Result(task.getResult()).setName(getName()));
                    }
                    return task.getResult();
                }, Task.UI_THREAD_EXECUTOR)
                .continueWith(task -> getData(), Task.BACKGROUND_EXECUTOR)
                .continueWith(task -> {
                    if (validate() && task.getResult() != null) {
                        getListener().response(task.getResult().setOrder(Result.LAST).setName(getName()));
                    }
                    return task.getResult();
                }, Task.UI_THREAD_EXECUTOR)
                .continueWith(new Continuation<Result<T>, Void>() {
                    public Void then(Task<Result<T>> task) {
                        if (validate() && task.getResult() != null && !task.getResult().hasError()) {
                            writeCache(task.getResult().getData());
                        }
                        return null;
                    }
                }, Task.BACKGROUND_EXECUTOR);
    }

    public abstract T readCache();

    public abstract void writeCache(T data);

}
