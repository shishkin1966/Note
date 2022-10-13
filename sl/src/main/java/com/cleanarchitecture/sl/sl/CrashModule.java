package com.cleanarchitecture.sl.sl;

import androidx.annotation.NonNull;

import com.cleanarchitecture.sl.data.Result;

/**
 * Модуль, протоколирующий Uncaught Exception
 */
public class CrashModule implements Thread.UncaughtExceptionHandler, Module {

    public static final String NAME = CrashModule.class.getName();
    private static Thread.UncaughtExceptionHandler mHandler = Thread.getDefaultUncaughtExceptionHandler();

    public CrashModule() {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        android.util.Log.e(NAME, throwable.getMessage(), throwable);
        ErrorModule.getInstance().onError(NAME, throwable);
        if (mHandler != null) {
            mHandler.uncaughtException(thread, throwable);
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isPersistent() {
        return false;
    }

    @Override
    public void onUnRegisterModule() {
    }

    @Override
    public Result<Boolean> validateExt() {
        return new Result<>(true);
    }

    @Override
    public boolean validate() {
        return validateExt().getData();
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (Thread.UncaughtExceptionHandler.class.isInstance(o)) ? 0 : 1;
    }

}

