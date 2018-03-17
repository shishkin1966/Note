package com.cleanarchitecture.sl.mail.impl;

import com.cleanarchitecture.sl.mail.AbsMail;
import com.cleanarchitecture.sl.mail.Mail;
import com.cleanarchitecture.sl.sl.MailSubscriber;
import com.cleanarchitecture.sl.sl.ObservableSubscriber;

/**
 * Created by Shishkin on 06.02.2018.
 */

public class OnChangeObservableMail<T> extends AbsMail {

    private static final String NAME = OnChangeObservableMail.class.getName();

    private T mObject;

    public OnChangeObservableMail(String address, T object) {
        super(address);

        mObject = object;
    }

    public OnChangeObservableMail(OnChangeObservableMail mail, T object) {
        super(mail);

        mObject = object;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void read(MailSubscriber subscriber) {
        if (ObservableSubscriber.class.isInstance(subscriber)) {
            ((ObservableSubscriber) subscriber).onChange(mObject);
        }
    }

    @Override
    public Mail copy() {
        return new OnChangeObservableMail(this, mObject);
    }

    @Override
    public boolean isCheckDublicate() {
        return true;
    }


}
