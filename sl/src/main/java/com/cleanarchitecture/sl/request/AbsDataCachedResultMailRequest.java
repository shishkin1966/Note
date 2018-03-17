package com.cleanarchitecture.sl.request;

import com.cleanarchitecture.sl.data.Result;
import com.cleanarchitecture.sl.mail.impl.ResultMail;
import com.cleanarchitecture.sl.sl.SLUtil;


import bolts.Continuation;
import bolts.Task;

/**
 * Created by Shishkin on 04.12.2017.
 */

public abstract class AbsDataCachedResultMailRequest<T> extends AbsDataResultMailRequest<T> implements ResultMailRequest {

    public AbsDataCachedResultMailRequest(final String listener) {
        super(listener);
    }

    @Override
    public void run() {
        if (!validate()) return;

        Task.call(() -> readCache())
                .continueWith(task -> {
                    if (validate() && task.getResult() != null) {
                        SLUtil.addMail(new ResultMail(getListener(), new Result(task.getResult()).setName(getName())).setName(getName()));
                    }
                    return task.getResult();
                })
                .continueWith(task -> getData())
                .continueWith(task -> {
                    if (validate() && task.getResult() != null) {
                        SLUtil.addMail(new ResultMail(getListener(), task.getResult().setOrder(Result.LAST).setName(getName())).setName(getName()));
                    }
                    return task.getResult();
                })
                .continueWith(new Continuation<Result<T>, Void>() {
                    public Void then(Task<Result<T>> task) {
                        if (validate() && task.getResult() != null && !task.getResult().hasError()) {
                            writeCache(task.getResult().getData());
                        }
                        return null;
                    }
                });
    }

    public abstract T readCache();

    public abstract void writeCache(T data);

}
