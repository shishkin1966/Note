package com.cleanarchitecture.sl.request;

import com.cleanarchitecture.sl.data.Result;
import com.cleanarchitecture.sl.mail.impl.ResultMail;
import com.cleanarchitecture.sl.sl.SLUtil;


import bolts.Continuation;
import bolts.Task;

/**
 * Created by Shishkin on 04.12.2017.
 */

public abstract class AbsDataResultMailRequest<T> extends AbsResultMailRequest implements ResultMailRequest {

    public AbsDataResultMailRequest(final String listener) {
        super(listener);
    }

    @Override
    public void run() {
        if (!validate()) return;

        Task.call(() -> getData())
                .continueWith(new Continuation<Result<T>, Void>() {
                    public Void then(Task<Result<T>> task) {
                        if (validate() && task.getResult() != null) {
                            SLUtil.addMail(new ResultMail(getListener(), task.getResult().setOrder(Result.LAST).setName(getName())).setName(getName()));
                        }
                        return null;
                    }
                });
    }

    public abstract Result<T> getData();
}
