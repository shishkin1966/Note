package com.cleanarchitecture.sl.request;

import com.cleanarchitecture.sl.data.Result;


import bolts.Continuation;
import bolts.Task;

/**
 * Created by Shishkin on 04.12.2017.
 */

public abstract class AbsDataResultRequest<T> extends AbsResultRequest<T> implements ResultRequest {

    public AbsDataResultRequest(final ResponseListener listener) {
        super(listener);
    }

    @Override
    public void run() {
        if (!validate()) return;

        Task.call(() -> getData(), Task.BACKGROUND_EXECUTOR)
                .continueWith(new Continuation<Result<T>, Void>() {
                    public Void then(Task<Result<T>> task) {
                        if (validate() && task.getResult() != null) {
                            getListener().response(task.getResult().setOrder(Result.LAST).setName(getName()));
                        }
                        return null;
                    }
                }, Task.UI_THREAD_EXECUTOR);
    }

    public abstract Result<T> getData();
}
