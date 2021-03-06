package com.cleanarchitecture.sl.sl;

import java.util.List;

/**
 * Интерфейс объекта, который регистрируется в модулях для получения/предоставления сервиса
 */
public interface ModuleSubscriber extends Subscriber, Validated {

    /**
     * Получить список имен модулей, в которых должен быть зарегистрирован объект
     *
     * @return список имен модулей
     */
    List<String> getModuleSubscription();

}
