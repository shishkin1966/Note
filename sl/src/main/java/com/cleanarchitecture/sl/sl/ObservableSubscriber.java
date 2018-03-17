package com.cleanarchitecture.sl.sl;

import java.util.List;

public interface ObservableSubscriber<T> extends MailSubscriber {

    /**
     * Получить список Observable объектов
     *
     * @return список имен Observable объектов
     */
    List<String> getObservable();

    /**
     * Событие - объект изменен
     */
    void onChange(T object);

}
