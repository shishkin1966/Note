package com.cleanarchitecture.sl.sl;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.cleanarchitecture.sl.BuildConfig;
import com.cleanarchitecture.sl.data.Result;
import com.cleanarchitecture.sl.handler.ApplicationLifecycleHandler;


import java.io.File;


/**
 * Модуль приложения
 */
public abstract class ApplicationModule extends MultiDexApplication implements IApplicationModule {

    public static final String NAME = ApplicationModule.class.getName();
    private static final String EXTERNAL_STORAGE_APPLICATION_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() +
            File.separator + BuildConfig.LIBRARY_PACKAGE_NAME + File.separator;
    private static String[] PERMISIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION};
    private static volatile ApplicationModule sInstance;
    private static volatile ApplicationLifecycleHandler sHandler;
    private boolean isFinished = false;
    private boolean isScreenOff = false;

    @Override
    public void onCreate() {
        super.onCreate();

        start();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static ApplicationModule getInstance() {
        return sInstance;
    }

    @Override
    public void onApplicationUpdated(final int oldVersion, final int newVersion) {
    }

    @Override
    public String getCachePath() {
        return getExternalCacheDir().getAbsolutePath();
    }

    @Override
    public String getDataPath() {
        return getFilesDir().getAbsolutePath() + File.separator;
    }

    @Override
    public String getExternalDataPath() {
        return EXTERNAL_STORAGE_APPLICATION_PATH;
    }

    @Override
    public String[] getRequiredPermisions() {
        return PERMISIONS;
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

    @Override
    public Result<Boolean> validateExt() {
        return new Result<>(true);
    }

    @Override
    public boolean validate() {
        return validateExt().getData();
    }

    public boolean isInBackground() {
        return sHandler.isInBackground();
    }

    @Override
    public int getVersion() {
        try {
            final PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionCode;
        } catch (Exception e) {
            return 0;
        }
    }

    private void start() {
        if (sInstance != null) {
            return;
        }

        sInstance = this;

        onStartApplication();
    }

    @Override
    public void onStartApplication() {

        SL.instantiate();

        sHandler = new ApplicationLifecycleHandler();
        registerActivityLifecycleCallbacks(sHandler);
        registerComponentCallbacks(sHandler);

        SLUtil.register(SLUtil.getUseCasesUnion().getScreenOnOffUseCase());
    }

    @Override
    public void finish() {
        isFinished = true;

        onFinish();

        SL.getInstance().onFinish();
    }

    @Override
    public void onFinish() {
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public void onBackgroundApplication() {
    }

    @Override
    public void onResumeApplication() {
    }

    @Override
    public void onScreenOff() {
        isScreenOff = true;
    }

    @Override
    public void onScreenOn() {
        isScreenOff = false;
    }

    public boolean isScreenOff() {
        return isScreenOff;
    }

    @Override
    public boolean isKillOnFinish() {
        return false;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (IApplicationModule.class.isInstance(o)) ? 0 : 1;
    }

}


