package com.cleanarchitecture.sl.sl;

import com.cleanarchitecture.sl.request.Request;
import com.cleanarchitecture.sl.request.ResponseListener;

/**
 * Интерфейс модуля выполнения запросов
 */
public interface IRequestModule extends Module {

    /**
     * Выполнить запрос параллельно
     *
     * @param request запрос
     */
    void request(Object sender, Request request);

    /**
     * Выполнить запрос последовательно
     *
     * @param request запрос
     */
    void requestSequentially(Object sender, Request request);

    /**
     * Отменить запросы слушателя
     *
     * @param listener слушатель
     */
    void cancelRequests(Object sender, ResponseListener listener);
}
