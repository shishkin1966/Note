package com.cleanarchitecture.sl.observe;

import com.cleanarchitecture.common.utils.StringUtils;
import com.cleanarchitecture.sl.data.Result;
import com.cleanarchitecture.sl.sl.MailSubscriber;
import com.cleanarchitecture.sl.sl.MailUnion;
import com.cleanarchitecture.sl.sl.SLUtil;
import com.cleanarchitecture.sl.state.ViewStateObserver;


import java.util.List;

/**
 * Created by Shishkin on 12.02.2018.
 */

public abstract class AbsMailObservable extends AbsObservable implements MailSubscriber, MailObservable {

    @Override
    public void register() {
        SLUtil.register(this);
    }

    @Override
    public void unregister() {
        SLUtil.unregister(this);
    }

    @Override
    public int getState() {
        return ViewStateObserver.STATE_RESUME;
    }

    @Override
    public void setState(int state) {
    }

    @Override
    public List<String> getModuleSubscription() {
        return StringUtils.arrayToList(MailUnion.NAME);
    }

    @Override
    public Result<Boolean> validateExt() {
        return new Result<>(true);
    }

    @Override
    public boolean validate() {
        return validateExt().getData();
    }
}
