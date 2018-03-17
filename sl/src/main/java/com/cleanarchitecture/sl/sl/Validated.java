package com.cleanarchitecture.sl.sl;

import com.cleanarchitecture.sl.data.Result;

/**
 * Интерфейс объекта, который может быть проверен на жизнеспособность
 */
public interface Validated {

    /**
     * Проверить работоспособность объекта и получить по возможности описание ошибки
     *
     * @return Result<Boolean> - ответ на проверку работоспособности
     */
    Result<Boolean> validateExt();

    /**
     * Проверить работоспособность объекта
     *
     * @return true - объект может обеспечивать свою функциональность
     */
    boolean validate();

}
