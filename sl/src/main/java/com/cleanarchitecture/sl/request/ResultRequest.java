package com.cleanarchitecture.sl.request;

/**
 * Created by Shishkin on 13.12.2017.
 */

public interface ResultRequest extends Request {

    /**
     * Получить слушателя запроса
     *
     * @return ResponseListener - слушатель запроса
     */
    ResponseListener getListener();

}
