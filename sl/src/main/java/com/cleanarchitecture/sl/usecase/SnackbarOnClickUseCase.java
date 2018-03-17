package com.cleanarchitecture.sl.usecase;

import android.content.Context;

import com.cleanarchitecture.sl.R;
import com.cleanarchitecture.sl.event.ui.OnSnackBarClickEvent;
import com.cleanarchitecture.sl.sl.ApplicationModule;
import com.cleanarchitecture.sl.sl.SLUtil;

/**
 * Команда - нажатие на кнопку в панели Snackbar
 */
public class SnackbarOnClickUseCase extends AbsUseCase {

    public static final String NAME = SnackbarOnClickUseCase.class.getName();

    public static void onClick(final OnSnackBarClickEvent event) {

        final String action = event.getText();
        final Context context = SLUtil.getContext();

        if (context != null) {
            if (action.equals(context.getString(R.string.exit))) {
                ApplicationModule.getInstance().finish();
            }
        }
    }

    @Override
    public String getName() {
        return NAME;
    }
}
