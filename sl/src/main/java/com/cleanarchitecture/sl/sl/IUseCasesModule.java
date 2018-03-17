package com.cleanarchitecture.sl.sl;

import com.cleanarchitecture.sl.event.ui.OnSnackBarClickEvent;
import com.cleanarchitecture.sl.usecase.ScreenOnOffUseCase;

/**
 * Интерфейс модуля пользовательской и бизнес логики
 */
public interface IUseCasesModule extends Module {

    /**
     * Событие - click на SnackBar кнопке
     *
     * @param event событие
     */
    void onSnackBarClick(OnSnackBarClickEvent event);

    /**
     * Получить обработчик включения/выключения экрана
     *
     * @return ScreenOnOffUseCase обработчик
     */
    ScreenOnOffUseCase getScreenOnOffUseCase();
}
