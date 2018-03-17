package com.cleanarchitecture.sl.presenter;

import android.os.Bundle;

import com.cleanarchitecture.common.utils.StringUtils;
import com.cleanarchitecture.sl.data.Result;
import com.cleanarchitecture.sl.sl.IPresenterUnion;
import com.cleanarchitecture.sl.sl.MailSubscriber;
import com.cleanarchitecture.sl.sl.MailUnion;
import com.cleanarchitecture.sl.sl.PresenterUnion;
import com.cleanarchitecture.sl.sl.SLUtil;
import com.cleanarchitecture.sl.state.ViewStateObserver;


import java.util.List;

public abstract class AbsPresenter<M> implements Presenter<M>, MailSubscriber {

    private M mModel = null;
    private ViewStateObserver mLifecycle = new ViewStateObserver(this);
    private boolean mLostStateData = false;

    public AbsPresenter() {
    }

    public AbsPresenter(M model) {
        setModel(model);
    }

    @Override
    public synchronized int getState() {
        return mLifecycle.getState();
    }

    @Override
    public synchronized void setState(final int state) {
        mLifecycle.setState(state);
    }

    @Override
    public void onCreateView() {
    }

    @Override
    public void onReadyView() {
        SLUtil.register(this);

        onStart();
    }

    @Override
    public void onResumeView() {
        SLUtil.readMail(this);
    }

    @Override
    public void onPauseView() {
    }

    @Override
    public void onDestroyView() {
        SLUtil.unregister(this);

        final IPresenterUnion union = SLUtil.getPresenterUnion();
        if (union != null) {
            if (!mLostStateData) {
                union.saveStateData(this, getStateData());
            } else {
                union.clearStateData(this);
            }
        }

        onStop();
    }

    @Override
    public synchronized void setModel(final M model) {
        mModel = model;
    }

    @Override
    public synchronized M getModel() {
        return mModel;
    }

    @Override
    public boolean validate() {
        return validateExt().getData();
    }

    @Override
    public Result<Boolean> validateExt() {
        return new Result<>(mLifecycle.getState() != ViewStateObserver.STATE_DESTROY);
    }

    @Override
    public List<String> getModuleSubscription() {
        return StringUtils.arrayToList(
                PresenterUnion.NAME,
                MailUnion.NAME
        );
    }

    @Override
    public Bundle getStateData() {
        return null;
    }

    public void setLostStateData(boolean lostStateData) {
        mLostStateData = lostStateData;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onModelUpdated() {
    }

    @Override
    public void onActivityCreated() {
    }


}
