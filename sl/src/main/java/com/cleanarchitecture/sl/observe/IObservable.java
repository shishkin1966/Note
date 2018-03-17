package com.cleanarchitecture.sl.observe;

import com.cleanarchitecture.sl.sl.ObservableSubscriber;
import com.cleanarchitecture.sl.sl.Subscriber;


import java.util.List;

/**
 * Created by Shishkin on 15.12.2017.
 */

public interface IObservable<T> extends Subscriber {

    /**
     * Добавить слушателя к слушаемому объекту
     *
     * @param subscriber слушатель
     */
    void addObserver(ObservableSubscriber subscriber);

    /**
     * Удалить слушателя у слушаемого объекта
     *
     * @param subscriber слушатель
     */
    void removeObserver(ObservableSubscriber subscriber);

    /**
     * Зарегестрировать слушаемый объект. Вызывается при появлении
     * первого слушателя
     */
    void register();

    /**
     * Отменить регистрацию слушаемого объекта. Вызывается при удалении
     * последнего слушателя
     */
    void unregister();

    /**
     * Событие - в слушаемом объекте произошли изменения
     *
     * @param object объект изменения
     */
    void onChange(T object);

    /**
     * Получить список слушателей
     *
     * @return список слушателей
     */
    List<ObservableSubscriber> getObserver();

}
