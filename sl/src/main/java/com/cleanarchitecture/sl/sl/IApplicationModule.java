package com.cleanarchitecture.sl.sl;

import android.content.Context;

/**
 * Интерфейс модуля приложения.
 */
public interface IApplicationModule extends Module {

    /**
     * Получить Context приложения
     *
     * @return Context приложения
     */
    Context getApplicationContext();

    /**
     * Получить путь хранения Cache на SDCard
     *
     * @return путь хранения Cache на SDCard
     */
    String getCachePath();

    /**
     * Получить путь хранения данных и журналов на SDCard
     *
     * @return путь хранения данных и журналов на SDCard
     */
    String getExternalDataPath();

    /**
     * Получить путь хранения данных и журналов
     *
     * @return путь хранения данных и журналов
     */
    String getDataPath();

    /**
     * Получить Permisions приложения
     *
     * @return the string [ ]
     */
    String[] getRequiredPermisions();

    /**
     * Событие - приложение обновлено
     *
     * @param oldVersion старая версия приложения
     * @param newVersion новая версия приложения
     */
    void onApplicationUpdated(int oldVersion, int newVersion);

    /**
     * Получить версию приложения
     *
     * @return версия приложения
     */
    int getVersion();

    /**
     * Флаг - приложение остановлено
     *
     * @return true = приложение остановлено
     */
    boolean isFinished();

    /**
     * Событие  - старт приложениея
     */
    void onStart();

    /**
     * Остановить приложение
     */
    void finish();

    /**
     * Событие  - остановка приложениея
     */
    void onFinish();

    /**
     * Событие - приложение уходит в Background
     */
    void onBackgroundApplication();

    /**
     * Событие - приложение выходит из Background
     */
    void onResumeApplication();

    /**
     * Флаг -  выгружать(kill) приложение при остановке(finish)
     */
    boolean isKillOnFinish();

    void onScreenOff();

    void onScreenOn();
}