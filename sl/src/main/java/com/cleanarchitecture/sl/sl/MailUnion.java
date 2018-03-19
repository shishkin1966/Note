package com.cleanarchitecture.sl.sl;

import android.support.annotation.NonNull;

import com.cleanarchitecture.common.utils.StreamUtils;
import com.cleanarchitecture.common.utils.StringUtils;
import com.cleanarchitecture.sl.data.Result;
import com.cleanarchitecture.sl.mail.Mail;
import com.cleanarchitecture.sl.state.ViewStateObserver;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Объединение, предоставляющее почтовый сервис подписчикам
 */
public class MailUnion extends AbsSmallUnion<MailSubscriber> implements IMailUnion {

    public static final String NAME = MailUnion.class.getName();
    private Map<Long, Mail> mMail = Collections.synchronizedMap(new ConcurrentHashMap<Long, Mail>());
    private AtomicLong mId = new AtomicLong(0L);
    private static volatile MailUnion sInstance;

    public static MailUnion getInstance() {
        if (sInstance == null) {
            synchronized (MailUnion.class) {
                if (sInstance == null) {
                    sInstance = new MailUnion();
                }
            }
        }
        return sInstance;
    }

    private MailUnion() {
        super();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public synchronized List<Mail> getMail(final MailSubscriber subscriber) {
        if (subscriber != null) {
            if (mMail.isEmpty()) {
                return new ArrayList<>();
            }

            // удаляем старые письма
            final String name = subscriber.getName();
            final long currentTime = System.currentTimeMillis();
            final List<Mail> list = StreamUtils.filter(mMail.values(), mail -> (mail.contains(name) && mail.getEndTime() != -1 && mail.getEndTime() < currentTime)).toList();
            if (!list.isEmpty()) {
                for (Mail mail : list) {
                    mMail.remove(mail.getId());
                }
            }

            if (mMail.isEmpty()) {
                return new ArrayList<>();
            }

            final Comparator<Mail> byId = (left, right) -> left.getId().compareTo(right.getId());
            return StreamUtils.filter(mMail.values(), mail -> mail.contains(name) && (mail.getEndTime() == -1 || (mail.getEndTime() != -1 && mail.getEndTime() > currentTime))).sorted(byId).toList();
        }
        return new ArrayList<>();
    }

    @Override
    public synchronized void clearMail(final MailSubscriber subscriber) {
        if (subscriber != null) {
            if (mMail.isEmpty()) {
                return;
            }

            final String name = subscriber.getName();
            final List<Mail> list = StreamUtils.filter(mMail.values(), mail -> mail.contains(name)).toList();
            if (!list.isEmpty()) {
                for (Mail mail : list) {
                    mMail.remove(mail.getId());
                }
            }
        }
    }

    @Override
    public synchronized void addMail(final Mail mail) {
        if (mail != null) {
            final List<String> list = mail.getCopyTo();
            list.add(mail.getAddress());
            for (String address : list) {
                final long id = mId.incrementAndGet();
                final Mail newMail = mail.copy();
                newMail.setId(id);
                newMail.setAddress(address);
                newMail.setCopyTo(new ArrayList<>());

                if (!mail.isCheckDublicate()) {
                    mMail.put(id, newMail);
                } else {
                    removeDublicate(newMail);
                    mMail.put(id, newMail);
                }

                checkAddMailSubscriber(address);
            }
        }
    }

    private void checkAddMailSubscriber(final String address) {
        if (StringUtils.isNullOrEmpty(address)) {
            return;
        }

        List<WeakReference<MailSubscriber>> list = getSubscribers();
        for (WeakReference<MailSubscriber> reference : list) {
            final MailSubscriber subscriber = reference.get();
            if (address.equalsIgnoreCase(subscriber.getName())) {
                if (subscriber.getState() == ViewStateObserver.STATE_RESUME) {
                    SLUtil.readMail(subscriber);
                }
            }
        }
    }

    private synchronized void removeDublicate(final Mail mail) {
        if (mail != null && !StringUtils.isNullOrEmpty(mail.getName()) && !StringUtils.isNullOrEmpty(mail.getAddress())) {
            for (Mail tmpMail : mMail.values()) {
                if (tmpMail != null) {
                    if (mail.getName().equals(tmpMail.getName()) && mail.getAddress().equals(tmpMail.getAddress())) {
                        removeMail(tmpMail);
                    }
                }
            }
        }
    }

    @Override
    public synchronized void removeMail(final Mail mail) {
        if (mail != null) {
            if (mMail.containsKey(mail.getId())) {
                mMail.remove(mail.getId());
            }
        }
    }

    @Override
    public synchronized void clearMail() {
        mMail.clear();
    }

    @Override
    public synchronized void onFinishApplication() {
        clearMail();
    }

    @Override
    public Result<Boolean> validateExt() {
        return new Result<>(super.validateExt().getData());
    }

    @Override
    public boolean isPersistent() {
        return true;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (IMailUnion.class.isInstance(o)) ? 0 : 1;
    }
}
