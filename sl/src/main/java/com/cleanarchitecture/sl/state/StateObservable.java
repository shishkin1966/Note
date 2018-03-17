package com.cleanarchitecture.sl.state;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StateObservable implements Stateable {
    private List<WeakReference<Stateable>> mList = Collections.synchronizedList(new ArrayList<WeakReference<Stateable>>());
    private int mState = ViewStateObserver.STATE_CREATE;

    public StateObservable(final int state) {
        setState(state);
    }

    @Override
    public synchronized void setState(final int state) {
        mState = state;
        for (WeakReference<Stateable> stateable : mList) {
            if (stateable != null && stateable.get() != null) {
                stateable.get().setState(mState);
            }
        }
    }

    public synchronized int getState() {
        return mState;
    }

    /**
     * Добавить слушателя состояний
     *
     * @param stateable слушатель состояний
     */
    public synchronized void addObserver(final Stateable stateable) {
        if (stateable != null) {
            for (WeakReference<Stateable> ref : mList) {
                if (ref != null && ref.get() != null) {
                    if (ref.get() == stateable) {
                        return;
                    }
                }
            }

            stateable.setState(mState);
            mList.add(new WeakReference<>(stateable));
        }
    }

    /**
     * Удалить слушателя состояний
     *
     * @param stateable слушатель состояний
     */
    public synchronized void removeObserver(final Stateable stateable) {
        for (WeakReference<Stateable> ref : mList) {
            if (ref != null && ref.get() != null) {
                if (ref.get() == stateable) {
                    mList.remove(ref);
                    return;
                }
            }
        }
    }

    /**
     * Удалить всех слушателей
     */
    public synchronized void clear() {
        mList.clear();
    }

}
