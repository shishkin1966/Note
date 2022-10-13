package com.cleanarchitecture.sl.presenter.impl;

import android.content.Context;

import com.cleanarchitecture.sl.R;
import com.cleanarchitecture.sl.event.ui.ShowMessageEvent;
import com.cleanarchitecture.sl.presenter.AbsPresenter;
import com.cleanarchitecture.sl.sl.ApplicationModule;
import com.cleanarchitecture.sl.sl.SLUtil;
import com.google.android.material.snackbar.Snackbar;


import java.util.Timer;
import java.util.TimerTask;

public class OnBackPressedPresenter extends AbsPresenter {
    private static final String NAME = OnBackPressedPresenter.class.getName();

    private boolean mDoubleBackPressedOnce = false;
    private Timer mTimer;

    public boolean onClick() {
        if (validate()) {
            if (!mDoubleBackPressedOnce) {
                final Context context = SLUtil.getContext();
                if (context != null) {
                    mDoubleBackPressedOnce = true;
                    SLUtil.getActivityUnion().showSnackbar(new ShowMessageEvent(context.getString(R.string.double_back_pressed)).setAction(context.getString(R.string.exit)).setDuration(Snackbar.LENGTH_SHORT));
                    startTimer();
                }
            } else {
                ApplicationModule.getInstance().finish();
                return true;
            }
        }
        return false;
    }

    private void startTimer() {
        if (mTimer != null) {
            stopTimer();
        }
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mDoubleBackPressedOnce = false;
            }
        }, 3000);
    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        stopTimer();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isRegister() {
        return false;
    }


}
