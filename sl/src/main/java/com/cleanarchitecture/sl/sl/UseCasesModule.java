package com.cleanarchitecture.sl.sl;

import androidx.annotation.NonNull;

import com.cleanarchitecture.sl.event.ui.OnSnackBarClickEvent;
import com.cleanarchitecture.sl.usecase.ScreenOnOffUseCase;
import com.cleanarchitecture.sl.usecase.SnackbarOnClickUseCase;

/**
 * Модуль реализующий бизнес и пользовательскую логику в приложении
 */
@SuppressWarnings("unused")
public class UseCasesModule extends AbsModule implements IUseCasesModule {
    public static final String NAME = UseCasesModule.class.getName();

    private ScreenOnOffUseCase mScreenOnOffUseCase;

    public UseCasesModule() {
        mScreenOnOffUseCase = new ScreenOnOffUseCase();
    }


    @Override
    public void onUnRegisterModule() {
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void onSnackBarClick(final OnSnackBarClickEvent event) {
        SnackbarOnClickUseCase.onClick(event);
    }

    @Override
    public ScreenOnOffUseCase getScreenOnOffUseCase() {
        return mScreenOnOffUseCase;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (IUseCasesModule.class.isInstance(o)) ? 0 : 1;
    }
}
