package com.cleanarchitecture.sl.task;

import com.cleanarchitecture.sl.request.ResponseListener;
import com.cleanarchitecture.sl.delegate.SenderDelegating;

/**
 * Created by Shishkin on 13.03.2018.
 */

public interface IExecutor extends SenderDelegating {

    void cancelRequests(ResponseListener listener);

    void shutdown();

    void clear();
}
