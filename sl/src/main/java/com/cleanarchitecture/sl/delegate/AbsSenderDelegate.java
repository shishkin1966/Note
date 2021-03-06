package com.cleanarchitecture.sl.delegate;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Shishkin on 07.03.2018.
 */

public abstract class AbsSenderDelegate<T extends SenderDelegating> implements SenderDelegate<T> {

    private Map<String, T> mDelegates = Collections.synchronizedMap(new ConcurrentHashMap<String, T>());
    private DelegatingFactory<T> mDelegateFactory = getDelegateFactory();

    public abstract DelegatingFactory<T> getDelegateFactory();

    @Override
    public void processing(Object sender, Object object) {
        if (sender != null) {
            if (!mDelegates.containsKey(sender.getClass().getName())) {
                final T delegate = mDelegateFactory.create(sender.getClass().getName());
                mDelegates.put(sender.getClass().getName(), delegate);
            }
            if (mDelegates.containsKey(sender.getClass().getName())) {
                final T delegating = mDelegates.get(sender.getClass().getName());
                if (delegating != null) {
                    delegating.processing(sender, object);
                }
            }
        }
    }

    @Override
    public T get(Object sender) {
        if (!mDelegates.containsKey(sender.getClass().getName())) {
            final T delegate = mDelegateFactory.create(sender.getClass().getName());
            mDelegates.put(sender.getClass().getName(), delegate);
        }
        if (mDelegates.containsKey(sender.getClass().getName())) {
            return (T) mDelegates.get(sender.getClass().getName());
        }
        return null;
    }

    @Override
    public List<T> getAll() {
        final ArrayList<T> list = new ArrayList<>();
        list.addAll(mDelegates.values());
        return list;
    }

}
