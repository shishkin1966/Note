package com.cleanarchitecture.sl.event.navigation;

import com.cleanarchitecture.sl.event.AbsEvent;

/**
 * Событие - выполнить команду "переключиться на фрагмент"
 */
public class SwitchToFragmentEvent extends AbsEvent {
    private String mName;

    public SwitchToFragmentEvent(final String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

}
