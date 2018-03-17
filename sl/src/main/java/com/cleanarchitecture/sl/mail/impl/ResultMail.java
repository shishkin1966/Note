package com.cleanarchitecture.sl.mail.impl;

import com.cleanarchitecture.common.utils.ApplicationUtils;
import com.cleanarchitecture.sl.data.Result;
import com.cleanarchitecture.sl.mail.AbsMail;
import com.cleanarchitecture.sl.mail.Mail;
import com.cleanarchitecture.sl.request.ResponseListener;
import com.cleanarchitecture.sl.sl.MailSubscriber;

/**
 * Created by Shishkin on 25.01.2018.
 */

public class ResultMail extends AbsMail {

    private Result mResult;
    private String mName;

    public ResultMail(final String address, final Result result) {
        super(address);

        mResult = result;
    }

    public ResultMail(ResultMail mail, final Result result, final String name) {
        super(mail);

        mResult = result;
        mName = name;
    }

    @Override
    public String getName() {
        return mName;
    }

    public ResultMail setName(String name) {
        this.mName = name;
        return this;
    }

    @Override
    public void read(MailSubscriber subscriber) {
        if (ResponseListener.class.isInstance(subscriber)) {
            ApplicationUtils.runOnUiThread(() -> ((ResponseListener) subscriber).response(mResult));
        }
    }

    @Override
    public Mail copy() {
        return new ResultMail(this, mResult, mName);
    }

    @Override
    public boolean isCheckDublicate() {
        return true;
    }


}
