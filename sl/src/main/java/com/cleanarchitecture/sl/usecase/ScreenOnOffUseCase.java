package com.cleanarchitecture.sl.usecase;

import android.content.Intent;

import com.cleanarchitecture.common.utils.StringUtils;
import com.cleanarchitecture.sl.observe.impl.ScreenBroadcastReceiverObservable;
import com.cleanarchitecture.sl.sl.ApplicationModule;
import com.cleanarchitecture.sl.sl.MailSubscriber;
import com.cleanarchitecture.sl.sl.MailUnion;
import com.cleanarchitecture.sl.sl.ObservableSubscriber;
import com.cleanarchitecture.sl.sl.ObservableUnion;
import com.cleanarchitecture.sl.state.ViewStateObserver;


import java.util.List;

/**
 * Команда - гашение/включение экрана
 */
public class ScreenOnOffUseCase extends AbsUseCase implements ObservableSubscriber<Intent>, MailSubscriber {

    public static final String NAME = ScreenOnOffUseCase.class.getName();

    public static void onScreenOff() {
        ApplicationModule.getInstance().onScreenOff();
    }

    public static void onScreenOn() {
        ApplicationModule.getInstance().onScreenOff();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public List<String> getObservable() {
        return StringUtils.arrayToList(ScreenBroadcastReceiverObservable.NAME);
    }

    @Override
    public void onChange(Intent intent) {
        final String strAction = intent.getAction();

        if (strAction.equals(Intent.ACTION_SCREEN_OFF)) {
            onScreenOff();
        } else {
            onScreenOn();
        }
    }

    @Override
    public List<String> getModuleSubscription() {
        return StringUtils.arrayToList(MailUnion.NAME, ObservableUnion.NAME);
    }

    @Override
    public int getState() {
        return ViewStateObserver.STATE_RESUME;
    }

    @Override
    public void setState(int state) {
    }
}
