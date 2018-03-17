package com.cleanarchitecture.sl.request;

import com.cleanarchitecture.sl.data.Result;
import com.cleanarchitecture.sl.sl.Validated;

/**
 * Created by Shishkin on 05.12.2017.
 */

public interface ResponseListener extends Validated {

    /**
     * Событие - пришел ответ с результатами запроса
     *
     * @param result - результат
     */
    void response(Result result);

}
