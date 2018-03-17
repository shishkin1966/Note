package com.cleanarchitecture.sl.ui.fragment;

import android.support.annotation.IdRes;
import android.view.View;

import com.cleanarchitecture.sl.model.ModelView;
import com.cleanarchitecture.sl.sl.Subscriber;
import com.cleanarchitecture.sl.ui.IView;
import com.cleanarchitecture.sl.ui.activity.IActivity;

/**
 * Интерфейс фрагмента
 */
@SuppressWarnings("unused")
public interface IFragment extends Subscriber, IView, ModelView {
    /**
     * Найти view во фрагменте
     *
     * @param <V> the type view
     * @param id  the id view
     * @return the view
     */
    <V extends View> V findView(@IdRes int id);

    /**
     * получить IActivitySubscriber фрагмента
     *
     * @return IActivitySubscriber фрагмента
     */
    IActivity getActivitySubscriber();

    /**
     * Показать Progress диалог
     */
    void showProgressDialog();

    /**
     * Скрыть Progress диалог
     */
    void hideProgressDialog();

    /**
     * Получить корневой View объект
     *
     * @return корневой View
     */
    View getRootView();

    /**
     * Показать модальный Progress Bar
     */
    void showModalProgressBar();

    /**
     * Скрыть модальный Progress Bar
     */
    void hideModalProgressBar();

    /**
     * Закрыть
     */
    void exit();

    /**
     * Получить Accent Color
     *
     * @return Accent Color
     */
    int getAccentColor();

    /**
     * Получить Primary Color
     *
     * @return Primary Color
     */
    int getPrimaryColor();
}
