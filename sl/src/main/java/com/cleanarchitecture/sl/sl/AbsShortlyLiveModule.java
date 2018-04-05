package com.cleanarchitecture.sl.sl;

import com.cleanarchitecture.sl.handler.AutoCompleteHandler;


import java.util.concurrent.TimeUnit;

/**
 * Абстрактный коротко живущий модуль
 */
public abstract class AbsShortlyLiveModule extends AbsModule implements AutoCompleteHandler.OnShutdownListener {

    private AutoCompleteHandler<Boolean> mServiceHandler;
    private static final TimeUnit TIMEUNIT = TimeUnit.SECONDS;
    private static final long TIMEUNIT_DURATION = 10L;

    public AbsShortlyLiveModule() {
        mServiceHandler = new AutoCompleteHandler<>("AbsShortlyLiveModule [" + getName() + "]");
        mServiceHandler.setOnShutdownListener(this);
        mServiceHandler.post(true);
        setShutdownTimeout(TIMEUNIT.toMillis(TIMEUNIT_DURATION));
    }

    public void setShutdownTimeout(final long shutdownTimeout) {
        if (shutdownTimeout > 0) {
            mServiceHandler.setShutdownTimeout(shutdownTimeout);
        }
    }

    public void post() {
        mServiceHandler.post(true);
    }

    @Override
    public void onShutdown(AutoCompleteHandler handler) {
        SL.getInstance().unregisterModule(getName());
    }
}
