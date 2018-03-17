package com.cleanarchitecture.sl.sl;

import android.view.LayoutInflater;

import com.cleanarchitecture.sl.event.navigation.ShowFragmentEvent;
import com.cleanarchitecture.sl.event.navigation.StartActivityEvent;
import com.cleanarchitecture.sl.event.navigation.StartActivityForResultEvent;
import com.cleanarchitecture.sl.event.navigation.StartChooseActivityEvent;
import com.cleanarchitecture.sl.ui.activity.AbsActivity;

/**
 * Интерфейс объединения, отвечающего за навигацию в приложении
 */
@SuppressWarnings("unused")
public interface IRoutingUnion extends Union<Router> {

    /**
     * Получить фрагмент по его id.
     *
     * @param <F> тип фрагмента
     * @param cls класс фрагмента
     * @param id  the id
     * @return фрагмент
     */
    <F> F getFragment(final Class<F> cls, final int id);

    /**
     * Показать фрагмент
     *
     * @param event событие
     */
    void showFragment(ShowFragmentEvent event);

    /**
     * Переключиться на фрагмент
     *
     * @param name имя фрагмента
     */
    void switchToFragment(String name);

    /**
     * Переключиться на top фрагмент
     */
    void switchToTopFragment();

    /**
     * Перейти по BackPress
     */
    void back();

    /**
     * Получить AbstractActivity
     *
     * @return the AbstractActivity
     */
    <C> C getActivity();

    /**
     * Получить LayoutInflater
     *
     * @return the LayoutInflater
     */
    LayoutInflater getInflater();

    /**
     * Получить AbstractActivity
     *
     * @param name имя activity
     * @return the AbstractActivity
     */
    <C> C getActivity(String name);

    /**
     * Получить AbstractActivity
     *
     * @param name     имя activity
     * @param validate флаг - проверять activity на валидность
     * @return the AbstractActivity
     */
    <C> C getActivity(String name, boolean validate);

    /**
     * Проверить наличие записей в BackStack
     *
     * @return true - записей нет
     */
    boolean isBackStackEmpty();

    /**
     * Получить кол-во записей в BackStack
     *
     * @return кол-во записей
     */
    int getBackStackEntryCount();

    /**
     * Проверить наличие Top фрагмента
     *
     * @return true - Top фрагмент есть
     */
    boolean hasTop();

    /**
     * Start Activity
     *
     * @param event событие
     */
    void startActivity(StartActivityEvent event);

    /**
     * Start Choose Activity
     *
     * @param event событие
     */
    void startChooseActivity(StartChooseActivityEvent event);

    /**
     * Start Activity for Result
     *
     * @param event событие
     */
    void startActivityForResult(StartActivityForResultEvent event);

    /**
     * Показать Activity
     *
     * @param activity Activity
     */
    void showActivity(AbsActivity activity);

}
