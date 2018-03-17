package com.cleanarchitecture.sl.presenter;

import android.os.Bundle;

import com.cleanarchitecture.sl.sl.ModuleSubscriber;
import com.cleanarchitecture.sl.state.ViewStateListener;

public interface Presenter<M> extends ViewStateListener, ModuleSubscriber {

    /**
     * Установить модель презентера
     *
     * @param model the model
     */
    void setModel(M model);

    /**
     * Получить модель презентера
     *
     * @return the model
     */
    M getModel();

    /**
     * Флаг - регистрировать презентер в объединении презентеров
     *
     * @return true - регистрировать (презентер - глобальный)
     */
    boolean isRegister();

    /**
     * Получить данные о состоянии презентера
     *
     * @return данные презентера для сохранения и восстановления состояния
     */
    Bundle getStateData();

    /**
     * Флаг - сохранять/стирать состояние при уничтожении презентера
     *
     * @param lostStateData true - сохранять состояние, false - стирать состояние
     */
    void setLostStateData(boolean lostStateData);

    /**
     * Событие - презентер перешел в состояние STATE_RESUME
     */
    void onStart();

    /**
     * Событие - презентер перешел в состояние STATE_DESTROY
     */
    void onStop();

    /**
     * Событие - обновлена модель
     */
    void onModelUpdated();

}
