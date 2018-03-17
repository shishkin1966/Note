package com.cleanarchitecture.sl.sl;

import java.util.List;

public interface DbObservableSubscriber<T> extends ObservableSubscriber<T> {

    /**
     * Получить список слушаемых таблиц
     *
     * @return список слушаемых таблиц
     */
    List<String> getTables();

}
