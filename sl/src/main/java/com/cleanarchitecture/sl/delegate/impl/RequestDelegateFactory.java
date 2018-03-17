package com.cleanarchitecture.sl.delegate.impl;

import com.cleanarchitecture.sl.delegate.DelegatingFactory;
import com.cleanarchitecture.sl.repository.IDbProvider;
import com.cleanarchitecture.sl.repository.INetImageProvider;
import com.cleanarchitecture.sl.repository.INetProvider;
import com.cleanarchitecture.sl.task.CommonThreadPoolExecutor;
import com.cleanarchitecture.sl.task.DbThreadPoolExecutor;
import com.cleanarchitecture.sl.task.IExecutor;
import com.cleanarchitecture.sl.task.NetThreadPoolExecutor;

/**
 * Created by Shishkin on 13.03.2018.
 */

public class RequestDelegateFactory implements DelegatingFactory<IExecutor> {
    @Override
    public IExecutor create(Object sender) {
        if (INetProvider.class.isInstance(sender)) {
            return NetThreadPoolExecutor.getInstance();
        } else if (INetImageProvider.class.isInstance(sender)) {
            return NetThreadPoolExecutor.getInstance();
        } else if (IDbProvider.class.isInstance(sender)) {
            return DbThreadPoolExecutor.getInstance();
        }
        return CommonThreadPoolExecutor.getInstance();
    }
}
