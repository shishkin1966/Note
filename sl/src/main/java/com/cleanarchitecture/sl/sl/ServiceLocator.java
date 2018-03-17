package com.cleanarchitecture.sl.sl;

/**
 * Итерфейс администратора(Service Locator)
 */
@SuppressWarnings("unused")
public interface ServiceLocator extends Subscriber {

    /**
     * Проверить существование модуля
     *
     * @param name имя модуля
     * @return true - модуль существует
     */
    boolean exists(final String name);

    /**
     * Получить модуль
     *
     * @param <C>  тип модуля
     * @param name имя модуля
     * @return модуль
     */
    <C> C get(String name);

    /**
     * Зарегистрировать модуль
     *
     * @param module модуль
     * @return флаг - операция завершена успешно
     */
    boolean registerModule(Module module);

    /**
     * Зарегистрировать модуль
     *
     * @param name имя класса модуля
     * @return флаг - операция завершена успешно
     */
    boolean registerModule(String name);

    /**
     * Отменить регистрацию модуля или объекта
     *
     * @param name имя модуля/объекта
     * @return флаг - операция завершена успешно
     */
    boolean unregisterModule(String name);

    /**
     * Зарегистрировать подписчика модуля
     *
     * @param subscriber подписчик модуля
     * @return флаг - операция завершена успешно
     */
    boolean register(ModuleSubscriber subscriber);

    /**
     * Отменить регистрацию подписчика модуля
     *
     * @param subscriber подписчик модуля
     * @return флаг - операция завершена успешно
     */
    boolean unregister(ModuleSubscriber subscriber);

    /**
     * Установить подписчика текущим
     *
     * @param subscriber подписчик
     * @return флаг - операция завершена успешно
     */
    boolean setCurrentSubscriber(ModuleSubscriber subscriber);

    /**
     * Остановить service locator
     */
    void onFinish();

    /**
     * Старт service locator
     */
    void onStart();

    ModuleFactory getModuleFactory();
}
