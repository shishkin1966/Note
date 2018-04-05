package com.cleanarchitecture.sl.sl;

import android.support.annotation.NonNull;

import com.cleanarchitecture.common.utils.StringUtils;
import com.cleanarchitecture.sl.observe.IObservable;
import com.cleanarchitecture.sl.observe.impl.NetworkBroadcastReceiverObservable;
import com.cleanarchitecture.sl.observe.impl.ScreenBroadcastReceiverObservable;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Объединение Observable объектов
 */
public class ObservableUnion extends AbsSmallUnion<ObservableSubscriber> implements IObservableUnion {

    public static final String NAME = ObservableUnion.class.getName();
    private Map<String, IObservable> mObservables = Collections.synchronizedMap(new ConcurrentHashMap<String, IObservable>());

    public ObservableUnion() {
        super();

        register(new ScreenBroadcastReceiverObservable());
        register(new NetworkBroadcastReceiverObservable());
    }

    @Override
    public void register(final IObservable observable) {
        if (observable == null) return;

        mObservables.put(observable.getName(), observable);
    }

    @Override
    public void unregister(String name) {
        if (StringUtils.isNullOrEmpty(name)) return;

        if (mObservables.containsKey(name)) {
            mObservables.get(name).unregister();
            mObservables.remove(name);
        }
    }

    @Override
    public void register(final ObservableSubscriber subscriber) {
        if (subscriber == null) return;

        super.register(subscriber);

        final List<String> list = subscriber.getObservable();
        if (list != null) {
            for (IObservable observable : mObservables.values()) {
                if (observable != null) {
                    final String name = observable.getName();
                    if (list.contains(name)) {
                        observable.addObserver(subscriber);
                    }
                }
            }
        }
    }

    @Override
    public void unregister(final ObservableSubscriber subscriber) {
        if (subscriber == null) return;

        super.unregister(subscriber);

        final List<String> list = subscriber.getObservable();
        if (list != null) {
            for (IObservable observable : mObservables.values()) {
                if (observable != null) {
                    if (list.contains(observable.getName())) {
                        observable.removeObserver(subscriber);
                    }
                }
            }
        }
    }

    @Override
    public void onUnRegisterModule() {
        for (IObservable observable : mObservables.values()) {
            if (observable != null) {
                observable.unregister();
            }
        }
        mObservables.clear();
    }

    @Override
    public IObservable get(final String name) {
        if (StringUtils.isNullOrEmpty(name)) return null;

        if (mObservables.containsKey(name)) {
            return mObservables.get(name);
        }
        return null;
    }

    @Override
    public List<IObservable> getObservables() {
        final List<IObservable> list = new ArrayList<>();
        for (IObservable observable : mObservables.values()) {
            if (observable != null) {
                list.add(observable);
            }
        }
        return list;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (IObservableUnion.class.isInstance(o)) ? 0 : 1;
    }
}
