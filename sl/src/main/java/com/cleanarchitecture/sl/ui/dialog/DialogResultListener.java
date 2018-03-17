package com.cleanarchitecture.sl.ui.dialog;

import com.cleanarchitecture.sl.event.ui.DialogResultEvent;
import com.cleanarchitecture.sl.sl.Validated;

/**
 * Created by Shishkin on 17.11.2017.
 */

public interface DialogResultListener extends Validated {
    void onDialogResult(DialogResultEvent event);
}
