package com.cleanarchitecture.sl.observe;

import com.cleanarchitecture.common.utils.StringUtils;
import com.cleanarchitecture.sl.mail.impl.OnChangeObservableMail;
import com.cleanarchitecture.sl.sl.DbObservableSubscriber;
import com.cleanarchitecture.sl.sl.ObservableSubscriber;
import com.cleanarchitecture.sl.sl.SLUtil;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Shishkin on 16.12.2017.
 */

public abstract class AbsDbObservable extends AbsObservable<String> {

    private Map<String, List<String>> mTables = Collections.synchronizedMap(new ConcurrentHashMap<String, List<String>>());

    @Override
    public synchronized void addObserver(ObservableSubscriber subscriber) {
        if (subscriber == null) return;

        super.addObserver(subscriber);

        if (DbObservableSubscriber.class.isInstance(subscriber)) {
            final List<String> list = ((DbObservableSubscriber) subscriber).getTables();
            for (String table : list) {
                if (!mTables.containsKey(table)) {
                    mTables.put(table, new ArrayList<>());
                }
                if (!mTables.get(table).contains(subscriber.getName())) {
                    mTables.get(table).add(subscriber.getName());
                }
            }
        }
    }

    @Override
    public synchronized void removeObserver(ObservableSubscriber subscriber) {
        if (subscriber == null) return;

        super.removeObserver(subscriber);

        if (DbObservableSubscriber.class.isInstance(subscriber)) {
            for (List<String> tables : mTables.values()) {
                if (tables.contains(subscriber.getName())) {
                    tables.remove(subscriber.getName());
                }
            }
        }
    }

    @Override
    public synchronized void onChange(String object) {

        if (StringUtils.isNullOrEmpty(object)) return;

        final List<String> tableSubscribers = mTables.get(object);
        for (String name : tableSubscribers) {
            SLUtil.addMail(new OnChangeObservableMail<>(name, object));
        }
    }

    @Override
    public void register() {
    }

    @Override
    public void unregister() {
    }
}
