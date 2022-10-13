package com.cleanarchitecture.sl.sl;

import android.Manifest;
import android.content.Context;
import androidx.annotation.NonNull;
import android.widget.Toast;

import com.cleanarchitecture.common.ui.BaseSnackbar;
import com.cleanarchitecture.common.utils.ApplicationUtils;
import com.cleanarchitecture.common.utils.StringUtils;
import com.cleanarchitecture.sl.BuildConfig;
import com.cleanarchitecture.sl.R;
import com.cleanarchitecture.sl.data.ExtError;
import com.cleanarchitecture.sl.data.Result;
import com.cleanarchitecture.sl.event.ui.ShowErrorMessageEvent;
import com.github.snowdream.android.util.FilePathGenerator;
import com.github.snowdream.android.util.Log;


import java.io.File;

/**
 * Модуль обработки ошибок
 */
public class ErrorModule implements IErrorModule {
    public static final String NAME = ErrorModule.class.getName();
    private static final long MAX_LOG_LENGTH = 2000000;//2Mb

    private static volatile ErrorModule sInstance;
    private ExtError mError;

    public static ErrorModule getInstance() {
        if (sInstance == null) {
            synchronized (ErrorModule.class) {
                if (sInstance == null) {
                    sInstance = new ErrorModule();
                }
            }
        }
        return sInstance;
    }

    private ErrorModule() {
        try {
            Log.setEnabled(true);
            Log.setLog2FileEnabled(true);
            String path;
            if (BuildConfig.DEBUG && ApplicationUtils.checkPermission(ApplicationModule.getInstance(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                path = ApplicationModule.getInstance().getExternalDataPath();
            } else {
                path = ApplicationModule.getInstance().getDataPath();
            }
            final File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            if (file.exists()) {
                Log.setFilePathGenerator(new FilePathGenerator.DefaultFilePathGenerator(path, "log", ".txt"));
                checkLogSize();
            } else {
                Log.setEnabled(false);
            }
        } catch (Exception e) {
            mError = new ExtError().addError(NAME, e.getMessage());
            Log.setEnabled(false);
        }
    }

    private void checkLogSize() {
        final String path = Log.getPath();

        try {
            final File file = new File(path);
            if (file.exists()) {
                if (file.length() == 0) {
                    Log.i(ApplicationUtils.getPhoneInfo());
                }

                if (file.length() > MAX_LOG_LENGTH) {
                    final String new_path = path + ".1";
                    final File new_file = new File(new_path);
                    if (new_file.exists()) {
                        new_file.delete();
                    }
                    file.renameTo(new_file);
                }
            }
        } catch (Exception e) {
            android.util.Log.e(NAME, e.getMessage());
        }
    }

    @Override
    public void onError(final String source, final Exception e) {
        if (BuildConfig.DEBUG) {
            ApplicationUtils.showToast(ApplicationModule.getInstance(), e.getMessage(), Toast.LENGTH_LONG, BaseSnackbar.MESSAGE_TYPE_ERROR);
            Log.e(source, e);
        } else {
            Log.e(source, e.getMessage());
        }
    }

    @Override
    public void onError(final String source, final Throwable throwable) {
        if (BuildConfig.DEBUG) {
            ApplicationUtils.showToast(ApplicationModule.getInstance(), throwable.getMessage(), Toast.LENGTH_LONG, BaseSnackbar.MESSAGE_TYPE_ERROR);
            Log.e(source, throwable);
        } else {
            Log.e(source, throwable.getMessage());
        }
    }

    @Override
    public void onError(final String source, final Exception e, final String displayMessage) {
        onError(source, e);

        if (!StringUtils.isNullOrEmpty(displayMessage)) {
            if (SLUtil.getActivityUnion().validate()) {
                SLUtil.getActivityUnion().showErrorMessage(new ShowErrorMessageEvent(displayMessage + getSufix()));
            }
        }
    }

    @Override
    public void onError(final String source, final String message, final boolean isDisplay) {
        if (!StringUtils.isNullOrEmpty(message)) {
            if (BuildConfig.DEBUG && !isDisplay) {
                ApplicationUtils.showToast(ApplicationModule.getInstance(), message, Toast.LENGTH_LONG, BaseSnackbar.MESSAGE_TYPE_ERROR);
            }
            Log.e(source, message);
            if (isDisplay) {
                if (SLUtil.getActivityUnion().validate()) {
                    SLUtil.getActivityUnion().showErrorMessage(new ShowErrorMessageEvent(message + getSufix()));
                }
            }
        }
    }

    @Override
    public void onError(final ExtError extError) {
        if (extError != null && extError.hasError()) {
            if (SLUtil.getActivityUnion().validate()) {
                SLUtil.getActivityUnion().showErrorMessage(new ShowErrorMessageEvent(extError.getErrorText() + getSufix()));
            }
        }
    }

    @Override
    public String getPath() {
        return Log.getPath();
    }

    @Override
    public void clearLog() {
        final File log = new File(getPath());
        if (log.exists()) {
            log.delete();
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isPersistent() {
        return true;
    }

    @Override
    public void onUnRegisterModule() {
    }

    private String getSufix() {
        final Context context = ApplicationModule.getInstance();
        if (context != null) {
            return context.getString(R.string.error_sufix);
        }
        return "";
    }

    @Override
    public Result<Boolean> validateExt() {
        return new Result<>(Log.isEnabled()).setError(mError);
    }

    @Override
    public boolean validate() {
        return validateExt().getData();
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (IErrorModule.class.isInstance(o)) ? 0 : 1;
    }

}
