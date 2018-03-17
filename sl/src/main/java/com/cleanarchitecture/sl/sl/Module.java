package com.cleanarchitecture.sl.sl;

/**
 * Интерфейс модуля - объекта предоставлющий сервис
 */
public interface Module extends Subscriber, Validated, Comparable {

    /**
     * Получить тип модуля
     *
     * @return true - не будет удаляться администратором
     */
    boolean isPersistent();

    /**
     * Событие - отключить регистрацию
     */
    void onUnRegisterModule();

}
