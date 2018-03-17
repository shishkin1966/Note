package com.cleanarchitecture.sl.ui.activity;

import android.support.annotation.IdRes;
import android.view.View;

import com.cleanarchitecture.sl.event.ui.DialogResultEvent;
import com.cleanarchitecture.sl.sl.ModuleSubscriber;
import com.cleanarchitecture.sl.state.Stateable;

/**
 * Интерфейс activity
 */
@SuppressWarnings("unused")
public interface IActivity extends ModuleSubscriber, Stateable {
    /**
     * Найти view в activity
     *
     * @param <V> the type view
     * @param id  the id view
     * @return the view
     */
    <V extends View> V findView(@IdRes final int id);

    /**
     * Установить цвет status bar телефона
     *
     * @param color цвет Status Bar
     */
    void setStatusBarColor(final int color);

    /**
     * Закрепить текущую ориентацию
     */
    void lockOrientation();

    /**
     * Закрепить ориентацию
     *
     * @param orientation ориентация
     */
    void lockOrientation(int orientation);

    /**
     * Разрешить любую ориентацию
     */
    void unlockOrientation();

    /**
     * Событие - закрыт диалог
     *
     * @param event событие
     */
    void onDialogResult(DialogResultEvent event);

    /**
     * Закрыть
     */
    void exit();

    /**
     * очистить Back Stack
     */
    void clearBackStack();

    /**
     * Событие - предоставлено право
     *
     * @param permission право
     */
    void onPermisionGranted(String permission);

    /**
     * Событие - право запрещено
     *
     * @param permission право
     */
    void onPermisionDenied(String permission);

    /**
     * Получить корневой View объект
     *
     * @return корневой View
     */
    View getRootView();

    /**
     * Получить Activity
     *
     * @return Activity activity
     */
    AbsActivity getActivity();

    /**
     * Показать Progress диалог
     */
    void showProgressDialog();

    /**
     * Показать Progress диалог
     *
     * @param message текст сообщения
     */
    void showProgressDialog(String message);

    /**
     * Скрыть Progress диалог
     */
    void hideProgressDialog();

    /**
     * Включить полноэкранный режим
     */
    void setFullScreen();

    /**
     * Скрыть клавиатуру
     */
    void hideKeyboard();

    /**
     * Показать клавиатуру
     */
    void showKeyboard(View view);

    /**
     * Показать клавиатуру
     *
     * @param mode режим отображения клавиатуры
     */
    void showKeyboard(View view, int mode);

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
