package com.cleanarchitecture.sl.usecase;

import com.cleanarchitecture.sl.data.Result;
import com.cleanarchitecture.sl.sl.Subscriber;
import com.cleanarchitecture.sl.sl.Validated;

public abstract class AbsUseCase implements Subscriber, Validated {

    public abstract String getName();

    @Override
    public Result<Boolean> validateExt() {
        return new Result<>(true);
    }

    @Override
    public boolean validate() {
        return validateExt().getData();
    }
}
