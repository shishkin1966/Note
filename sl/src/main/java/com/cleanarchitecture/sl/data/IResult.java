package com.cleanarchitecture.sl.data;

/**
 * Created by Shishkin on 15.12.2017.
 */

public interface IResult extends Data {

    /**
     * Проверить наличие ошибки в результате
     *
     * @return true - результат имеет ошибку
     */
    boolean hasError();
}
