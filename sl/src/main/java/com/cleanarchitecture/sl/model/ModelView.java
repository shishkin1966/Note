package com.cleanarchitecture.sl.model;

import androidx.annotation.IdRes;
import android.view.View;

import com.cleanarchitecture.sl.sl.Validated;
import com.cleanarchitecture.sl.state.Stateable;
import com.cleanarchitecture.sl.ui.IView;

public interface ModelView extends IView, Validated {

    /**
     * Добавить слушателя к ModelView объекту
     *
     * @param stateable stateable объект
     */
    void addStateObserver(final Stateable stateable);

    /**
     * Найти view в ModelView объекте
     *
     * @param <V> the type view
     * @param id  the id view
     * @return the view
     */
    <V extends View> V findView(@IdRes final int id);

    /**
     * Получить корневой view в ModelView объекте
     *
     * @return the view
     */
    View getRootView();

    /**
     * Показать Progress диалог
     */
    void showProgressDialog();

    /**
     * Скрыть Progress диалог
     */
    void hideProgressDialog();

    /**
     * Закрыть ModelView объект
     */
    void exit();

    /**
     * Получить модель
     *
     * @return модель
     */
    Model getModel();

    /**
     * Установить модель
     *
     * @param model модель
     */
    void setModel(Model model);
}
