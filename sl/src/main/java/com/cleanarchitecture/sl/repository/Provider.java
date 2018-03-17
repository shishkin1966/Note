package com.cleanarchitecture.sl.repository;

import com.cleanarchitecture.sl.request.Request;
import com.cleanarchitecture.sl.request.ResponseListener;
import com.cleanarchitecture.sl.sl.Module;

/**
 * Created by Shishkin on 04.12.2017.
 */

public interface Provider extends Module {

    /**
     * Выполнить запрос
     *
     * @param request запрос
     */
    void request(Request request);

    /**
     * Прервать все запросы слушателя
     *
     * @param listener слушатель
     */
    void cancelRequests(ResponseListener listener);

}
