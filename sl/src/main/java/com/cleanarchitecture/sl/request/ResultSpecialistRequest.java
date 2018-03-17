package com.cleanarchitecture.sl.request;

/**
 * Created by Shishkin on 13.12.2017.
 */

public interface ResultSpecialistRequest extends Request {

    /**
     * Получить имя специалиста, слушающего запрос
     *
     * @return имя специалиста, слушающего запрос
     */
    String getSpecialist();

}
