package com.cleanarchitecture.sl.state;

public interface Stateable {

    /**
     * Получить состояние объекта
     *
     * @return the state
     */
    int getState();

    /**
     * Установить состояние объекта
     *
     * @param state the state
     */
    void setState(int state);

}
