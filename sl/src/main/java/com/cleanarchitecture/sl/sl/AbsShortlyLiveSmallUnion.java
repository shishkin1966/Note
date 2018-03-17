package com.cleanarchitecture.sl.sl;

import com.cleanarchitecture.sl.handler.AutoCompleteHandler;


import java.util.concurrent.TimeUnit;

/**
 * Абстрактный коротко живущее малое объединение
 */
public abstract class AbsShortlyLiveSmallUnion<T extends ModuleSubscriber> extends AbsSmallUnion<T> implements SmallUnion<T>, AutoCompleteHandler.OnShutdownListener {

    private AutoCompleteHandler<Boolean> mServiceHandler;
    private static final TimeUnit TIMEUNIT = TimeUnit.SECONDS;
    private static final long TIMEUNIT_DURATION = 10L;

    public AbsShortlyLiveSmallUnion() {
        mServiceHandler = new AutoCompleteHandler<>("AbsShortlyLiveSmallUnion [" + getName() + "]");
        mServiceHandler.setOnShutdownListener(this);
        setShutdownTimeout(TIMEUNIT.toMillis(TIMEUNIT_DURATION));
    }

    public void setShutdownTimeout(final long shutdownTimeout) {
        if (shutdownTimeout > 0) {
            mServiceHandler.setShutdownTimeout(shutdownTimeout);
        }
    }

    @Override
    public synchronized void onUnRegisterLastSubscriber() {
        mServiceHandler.post(true);
    }

    @Override
    public synchronized void onShutdown(AutoCompleteHandler handler) {
        if (!hasSubscribers()) {
            SL.getInstance().unregisterModule(getName());
        }
    }
}
