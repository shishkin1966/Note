package com.cleanarchitecture.sl.sl;

import android.view.LayoutInflater;

import com.cleanarchitecture.sl.event.ui.ShowDialogEvent;
import com.cleanarchitecture.sl.event.ui.ShowEditDialogEvent;
import com.cleanarchitecture.sl.event.ui.ShowErrorMessageEvent;
import com.cleanarchitecture.sl.event.ui.ShowKeyboardEvent;
import com.cleanarchitecture.sl.event.ui.ShowListDialogEvent;
import com.cleanarchitecture.sl.event.ui.ShowMessageEvent;
import com.cleanarchitecture.sl.event.ui.ShowProgressDialogEvent;
import com.cleanarchitecture.sl.ui.activity.IActivity;

/**
 * Интерфейс объединения Activity
 */
@SuppressWarnings("unused")
public interface IActivityUnion extends Union<IActivity>, ModuleSubscriber, MailSubscriber {

    /**
     * Контроллировать право приложения
     *
     * @param permission право приложения
     * @return the boolean флаг - право приложению предоставлено
     */
    boolean checkPermission(String permission);

    /**
     * Запросить предоставление права приложению
     *
     * @param permission  право приложения
     * @param helpMessage сообщение, выводимое в диалоге предоставления права
     */
    void grantPermission(String permission, String helpMessage);

    /**
     * Контролировать наличие и текущую версию Google Play Services
     *
     * @return the boolean
     */
    void checkGooglePlayServices();

    /**
     * Обрабатывает событие - показать сообщение на экран
     *
     * @param event событие
     */
    void showSnackbar(ShowMessageEvent event);

    /**
     * Обрабатывает событие - показать диалог об ошибке на экран
     *
     * @param event событие
     */
    void showErrorMessage(ShowErrorMessageEvent event);

    /**
     * Обрабатывает событие - показать Toast на экран
     *
     * @param event событие
     */
    void showToast(ShowMessageEvent event);

    /**
     * Обрабатывает событие - скрыть клавиатуру
     */
    void hideKeyboard();

    /**
     * Обрабатывает событие - показать клавиатуру
     *
     * @param event событие
     */
    void showKeyboard(ShowKeyboardEvent event);

    /**
     * Обрабатывает событие - показать Progress Bar
     */
    void showProgressBar();

    /**
     * Обрабатывает событие - скрыть Progress Bar
     */
    void hideProgressBar();

    /**
     * Обрабатывает событие - показать Progress Dialog
     *
     * @param event событие
     */
    void showProgressDialog(ShowProgressDialogEvent event);

    /**
     * Обрабатывает событие - скрыть Progress Dialog
     */
    void hideProgressDialog();

    /**
     * Обрабатывает событие - показать диалок с выбором из списка
     *
     * @param event событие
     */
    void showListDialog(ShowListDialogEvent event);

    /**
     * Обрабатывает событие - показать диалок с редактированием параметра
     *
     * @param event событие
     */
    void showEditDialog(ShowEditDialogEvent event);

    /**
     * Обрабатывает событие - показать диалок
     *
     * @param event событие
     */
    void showDialog(ShowDialogEvent event);

    /**
     * Показать настройки экрана блокировки
     */
    void showLockScreenSetting();

    /**
     * Проверить разрешение на геолокацию
     */
    void checkLocationService();

    /**
     * Получить LayoutInflater
     *
     * @return the LayoutInflater
     */
    LayoutInflater getInflater();
}
