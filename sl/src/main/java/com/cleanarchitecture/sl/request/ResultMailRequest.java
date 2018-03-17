package com.cleanarchitecture.sl.request;

/**
 * Created by Shishkin on 13.12.2017.
 */

public interface ResultMailRequest extends Request {

    /**
     * Получить имя слушателя запроса
     *
     * @return имя слушателя запроса
     */
    String getListener();

}
