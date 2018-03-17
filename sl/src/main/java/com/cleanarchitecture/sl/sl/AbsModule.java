package com.cleanarchitecture.sl.sl;

import android.content.Context;

import com.cleanarchitecture.sl.data.ErrorCode;
import com.cleanarchitecture.sl.data.Result;

/**
 * Абстрактный модуль - объект предоставлющий сервис
 */
public abstract class AbsModule implements Module {

    private static final String NAME = AbsModule.class.getName();

    @Override
    public void onUnRegisterModule() {
    }

    @Override
    public boolean isPersistent() {
        return false;
    }

    @Override
    public Result<Boolean> validateExt() {
        final Context context = ApplicationModule.getInstance();
        if (context == null) {
            return new Result<>(false).setError(NAME, ErrorCode.ERROR_APPLICATION_CONTEXT);
        } else {
            return new Result<>(true);
        }
    }

    @Override
    public boolean validate() {
        return validateExt().getData();
    }

}
