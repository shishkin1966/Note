package com.cleanarchitecture.sl.data;

/**
 * Created by Shishkin on 05.12.2017.
 */

public interface Data {

    /**
     * Проверить работоспособность объекта
     *
     * @return true - объект может обеспечивать свою функциональность
     */
    boolean validate();

    /**
     * Проверить является ли объект пустым
     *
     * @return true - объект пуст
     */
    boolean isEmpty();
}
