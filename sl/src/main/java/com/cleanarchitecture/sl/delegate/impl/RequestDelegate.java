package com.cleanarchitecture.sl.delegate.impl;

import com.cleanarchitecture.sl.delegate.AbsSenderDelegate;
import com.cleanarchitecture.sl.delegate.DelegatingFactory;
import com.cleanarchitecture.sl.task.IExecutor;

/**
 * Created by Shishkin on 13.03.2018.
 */

public class RequestDelegate extends AbsSenderDelegate<IExecutor> {

    @Override
    public DelegatingFactory<IExecutor> getDelegateFactory() {
        return new RequestDelegateFactory();
    }
}
