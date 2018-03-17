package com.cleanarchitecture.sl.sl;

import android.os.Bundle;

import com.cleanarchitecture.sl.presenter.Presenter;

/**
 * Интерфейс объединения презенторов
 */
@SuppressWarnings("unused")
public interface IPresenterUnion extends Union<Presenter> {

    /**
     * Сохранить состояние presenter
     *
     * @param presenter presenter
     * @param state     состояние
     */
    void saveStateData(Presenter presenter, Bundle state);

    /**
     * Получить состояние presenter
     *
     * @param presenter presenter
     * @return состояние presenter
     */
    Bundle restoreStateData(Presenter presenter);

    /**
     * Очистить состояние presenter
     *
     * @param presenter presenter
     */
    void clearStateData(Presenter presenter);

    /**
     * Очистить состояние presenters
     */
    void clearStateData();

    /**
     * Получить presenter
     *
     * @param name имя презентера
     * @return презентер
     */
    <C> C getPresenter(final String name);

}
