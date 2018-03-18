package com.cleanarchitecture.sl.sl;

import com.cleanarchitecture.common.utils.StringUtils;
import com.cleanarchitecture.sl.data.Result;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Абстрактное малое объединение
 */
public abstract class AbsSmallUnion<T extends ModuleSubscriber> extends AbsModule implements SmallUnion<T> {

    private static final String NAME = AbsSmallUnion.class.getName();

    private Map<String, WeakReference<T>> mSubscribers = Collections.synchronizedMap(new ConcurrentHashMap<String, WeakReference<T>>());

    private void checkNullSubscriber() {
        for (Map.Entry<String, WeakReference<T>> entry : mSubscribers.entrySet()) {
            if (entry.getValue() == null || entry.getValue().get() == null) {
                mSubscribers.remove(entry.getKey());
            }
        }
    }

    @Override
    public synchronized void register(final T subscriber) {
        if (subscriber == null) {
            return;
        }

        if (!subscriber.validate()) {
            ErrorModule.getInstance().onError(NAME, "Registration not valid subscriber: " + subscriber.toString(), false);
        }

        checkNullSubscriber();

        final int cnt = mSubscribers.values().size();

        mSubscribers.put(subscriber.getName(), new WeakReference<>(subscriber));

        if (cnt == 0 && mSubscribers.size() == 1) {
            onRegisterFirstSubscriber();
        }
        onAddSubscriber(subscriber);
    }

    @Override
    public synchronized void unregister(final T subscriber) {
        if (subscriber == null) {
            return;
        }

        checkNullSubscriber();

        final int cnt = mSubscribers.values().size();
        if (mSubscribers.containsKey(subscriber.getName())) {
            if (subscriber.equals(mSubscribers.get(subscriber.getName()).get())) {
                mSubscribers.remove(subscriber.getName());
            }
        }

        if (cnt == 1 && mSubscribers.size() == 0) {
            onUnRegisterLastSubscriber();
        }
    }

    @Override
    public void onAddSubscriber(final T subscriber) {
    }

    @Override
    public void onRegisterFirstSubscriber() {
    }

    @Override
    public void onUnRegisterLastSubscriber() {
    }

    @Override
    public synchronized List<WeakReference<T>> getSubscribers() {
        checkNullSubscriber();

        final List<WeakReference<T>> list = new ArrayList<>();
        list.addAll(mSubscribers.values());
        return list;
    }

    @Override
    public synchronized T getSubscriber(final String name) {
        checkNullSubscriber();

        if (StringUtils.isNullOrEmpty(name)) return null;

        //if (mSubscribers.isEmpty()) {
        //    ErrorModule.getInstance().onError(getName(), "Subscribers not found (subscriber " + name + ")", false);
        //}

        if (!mSubscribers.containsKey(name)) {
            //ErrorModule.getInstance().onError(getName(), "Subscriber " + name + " not found", false);
            return null;
        }

        return mSubscribers.get(name).get();
    }

    @Override
    public synchronized boolean hasSubscribers() {
        checkNullSubscriber();

        return (!mSubscribers.isEmpty());
    }

    @Override
    public synchronized Result<Boolean> validateExt(final String name) {
        final T subscriber = getSubscriber(name);
        if (subscriber != null) {
            return subscriber.validateExt();
        }
        return new Result<>(false);
    }

    @Override
    public synchronized boolean validate(final String name) {
        return validateExt().getData();
    }

    @Override
    public synchronized void onFinishApplication() {
    }
}
