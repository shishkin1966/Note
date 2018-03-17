package com.cleanarchitecture.sl.sl;

import com.cleanarchitecture.sl.data.ExtError;

/**
 * Интерфейс модуля ошибок
 */
public interface IErrorModule extends Module {

    /**
     * Получить путь к логу приложения
     *
     * @return путь к логу приложения
     */
    public String getPath();

    /**
     * Удалить файл лога
     */
    void clearLog();

    /**
     * Ошибка
     *
     * @param source источник ошибки
     * @param e      Exception
     */
    void onError(String source, Exception e);

    /**
     * Ошибка
     *
     * @param source    источник ошибки
     * @param throwable Throwable
     */
    void onError(String source, Throwable throwable);

    /**
     * Ошибка
     *
     * @param source         источник ошибки
     * @param e              Exception
     * @param displayMessage текст ошибки пользователю
     */
    void onError(String source, Exception e, String displayMessage);

    /**
     * Ошибка
     *
     * @param source    источник ошибки
     * @param message   текст ошибки пользователю
     * @param isDisplay true - отображать на сообщение на дисплее, false - сохранять в журнале
     */
    void onError(final String source, final String message, final boolean isDisplay);

    /**
     * Ошибка
     *
     * @param extError ошибка
     */
    void onError(final ExtError extError);
}
