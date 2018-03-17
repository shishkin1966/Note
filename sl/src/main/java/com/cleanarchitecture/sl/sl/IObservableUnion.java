package com.cleanarchitecture.sl.sl;

import com.cleanarchitecture.sl.observe.IObservable;


import java.util.List;

/**
 * Интерфейс объединения Observable объектов
 */
public interface IObservableUnion extends SmallUnion<ObservableSubscriber> {

    /**
     * Зарегестрировать слушаемый объект
     *
     * @param observable слушаемый (IObservable) объект
     */
    void register(IObservable observable);

    /**
     * Отменить регистрацию слушаемого объекта
     *
     * @param name имя слушаемого (IObservable) объекта
     */
    void unregister(String name);

    /**
     * Получить слушаемый объект
     *
     * @param name имя слушаемого (IObservable) объекта
     * @return слушаемый(IObservable) объект
     */
    IObservable get(final String name);

    /**
     * Получить список слушаемых объектов
     *
     * @return список слушаемых(IObservable) объектов
     */
    List<IObservable> getObservables();
}
