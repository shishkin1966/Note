package com.cleanarchitecture.sl.observe;

import com.cleanarchitecture.sl.mail.impl.OnChangeObservableMail;
import com.cleanarchitecture.sl.sl.ObservableSubscriber;
import com.cleanarchitecture.sl.sl.SLUtil;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Shishkin on 15.12.2017.
 */

public abstract class AbsObservable<T> implements IObservable<T> {
    private Map<String, WeakReference<ObservableSubscriber>> mObservers = Collections.synchronizedMap(new ConcurrentHashMap<String, WeakReference<ObservableSubscriber>>());

    private void checkNullObserver() {
        for (Map.Entry<String, WeakReference<ObservableSubscriber>> entry : mObservers.entrySet()) {
            if (entry.getValue() == null || entry.getValue().get() == null) {
                mObservers.remove(entry.getKey());
            }
        }
    }

    @Override
    public void addObserver(ObservableSubscriber subscriber) {
        if (subscriber == null) return;

        checkNullObserver();

        if (mObservers.isEmpty()) {
            register();
        }

        mObservers.put(subscriber.getName(), new WeakReference<>(subscriber));
    }

    @Override
    public void removeObserver(ObservableSubscriber subscriber) {
        if (subscriber == null) return;

        checkNullObserver();

        if (mObservers.containsKey(subscriber.getName())) {
            if (subscriber.equals(mObservers.get(subscriber.getName()).get())) {
                mObservers.remove(subscriber.getName());
            }

            if (mObservers.isEmpty()) {
                unregister();
            }
        }
    }

    @Override
    public void onChange(T object) {
        for (WeakReference<ObservableSubscriber> ref : mObservers.values()) {
            if (ref != null && ref.get() != null) {
                SLUtil.addMail(new OnChangeObservableMail<T>(ref.get().getName(), object));
            }
        }
    }

    @Override
    public List<ObservableSubscriber> getObserver() {
        final List<ObservableSubscriber> list = new ArrayList<>();

        for (WeakReference<ObservableSubscriber> ref : mObservers.values()) {
            if (ref != null && ref.get() != null) {
                list.add(ref.get());
            }
        }
        return list;
    }

}
