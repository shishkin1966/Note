package com.cleanarchitecture.sl.mail.impl;

import com.cleanarchitecture.sl.event.ui.DialogResultEvent;
import com.cleanarchitecture.sl.mail.AbsMail;
import com.cleanarchitecture.sl.mail.Mail;
import com.cleanarchitecture.sl.sl.MailSubscriber;
import com.cleanarchitecture.sl.ui.dialog.DialogResultListener;

/**
 * Created by Shishkin on 31.01.2018.
 */

public class DialogResultMail extends AbsMail {

    private static final String NAME = DialogResultMail.class.getName();

    private DialogResultEvent mEvent;

    public DialogResultMail(final String address, final DialogResultEvent event) {
        super(address);

        mEvent = event;
    }

    public DialogResultMail(final DialogResultMail mail, final DialogResultEvent event) {
        super(mail);

        mEvent = event;
    }

    @Override
    public Mail copy() {
        return new DialogResultMail(this, mEvent);
    }


    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void read(MailSubscriber subscriber) {
        if (DialogResultListener.class.isInstance(subscriber)) {
            ((DialogResultListener) subscriber).onDialogResult(mEvent);
        }
    }
}
