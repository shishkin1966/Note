package com.cleanarchitecture.sl.repository;

import android.Manifest;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import android.content.Context;
import androidx.annotation.NonNull;
import android.widget.Toast;

import com.cleanarchitecture.common.ui.BaseSnackbar;
import com.cleanarchitecture.common.utils.ApplicationUtils;
import com.cleanarchitecture.common.utils.SafeUtils;
import com.cleanarchitecture.common.utils.StringUtils;
import com.cleanarchitecture.sl.R;
import com.cleanarchitecture.sl.data.Result;
import com.cleanarchitecture.sl.request.Request;
import com.cleanarchitecture.sl.request.ResponseListener;
import com.cleanarchitecture.sl.sl.ApplicationModule;
import com.cleanarchitecture.sl.sl.ErrorModule;
import com.cleanarchitecture.sl.sl.SLUtil;
import com.google.common.io.Files;


import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Shishkin on 19.12.2017.
 */

public class DbProvider<T extends RoomDatabase> extends AbsProvider implements IDbProvider<T> {

    public static final String NAME = DbProvider.class.getName();
    private Map<String, T> mDb = Collections.synchronizedMap(new ConcurrentHashMap<String, T>());
    private RoomDatabase.Callback mCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            onCreateDatabase(db);
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            onOpenDatabase(db);
        }
    };

    public DbProvider() {
    }

    private boolean connect(final Class<T> klass, final String databaseName, Migration... migrations) {
        final Context context = SLUtil.getContext();
        if (context == null) {
            return false;
        }

        try {
            T db;
            if (migrations == null) {
                db = Room.databaseBuilder(context, klass, databaseName)
                        .addCallback(mCallback)
                        .build();
            } else {
                db = Room.databaseBuilder(context, klass, databaseName)
                        .addMigrations(migrations)
                        .addCallback(mCallback)
                        .build();
            }
            if (db != null) {
                db.getOpenHelper().getReadableDatabase().getVersion();
            }
            mDb.put(databaseName, db);
        } catch (Exception e) {
            ErrorModule.getInstance().onError(NAME, e);
        }
        return isConnected(databaseName);
    }

    private boolean disconnect(final String databaseName) {
        if (isConnected(databaseName)) {
            final T db = mDb.get(databaseName);
            try {
                db.close();
                mDb.remove(databaseName);
            } catch (Exception e) {
                ErrorModule.getInstance().onError(NAME, e);
            }
        }
        return !isConnected(databaseName);
    }

    private boolean isConnected(final String databaseName) {
        if (StringUtils.isNullOrEmpty(databaseName)) {
            return false;
        }

        return mDb.containsKey(databaseName);
    }

    @Override
    public void backup(final String databaseName, final String dirBackup) {
        final T db = mDb.get(databaseName);
        if (db == null) {
            return;
        }

        final Class<T> klass = SafeUtils.cast(db.getClass().getSuperclass());
        final String pathDb = db.getOpenHelper().getReadableDatabase().getPath();
        if (StringUtils.isNullOrEmpty(pathDb)) {
            return;
        }

        if (!ApplicationUtils.checkPermission(ApplicationModule.getInstance(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ApplicationUtils.runOnUiThread(() -> ApplicationUtils.grantPermisions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, SLUtil.getActivity()));
            return;
        }

        disconnect(databaseName);

        final File fileDb = new File(pathDb);
        final String nameDb = fileDb.getName();
        final String pathBackup = dirBackup + File.separator + nameDb;
        try {
            final File fileBackup = new File(pathBackup);
            final File fileBackupOld = new File(pathBackup + "1");
            if (fileDb.exists()) {
                if (fileBackup.exists()) {
                    final File dir = new File(dirBackup);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    if (dir.exists()) {
                        if (fileBackupOld.exists()) {
                            fileBackupOld.delete();
                        }
                        if (!fileBackupOld.exists()) {
                            Files.copy(fileBackup, fileBackupOld);
                            if (fileBackupOld.exists()) {
                                fileBackup.delete();
                                if (!fileBackup.exists()) {
                                    Files.copy(fileDb, fileBackup);
                                    if (fileBackup.exists()) {
                                        fileBackupOld.delete();
                                    } else {
                                        Files.copy(fileBackupOld, fileBackup);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    final File dir = new File(dirBackup);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    if (dir.exists()) {
                        Files.copy(fileDb, fileBackup);
                    }
                }
            }

            connect(klass, nameDb);

            ApplicationUtils.runOnUiThread(() -> ApplicationUtils.showToast(ApplicationModule.getInstance(), ApplicationModule.getInstance().getString(R.string.db_backuped), Toast.LENGTH_LONG, BaseSnackbar.MESSAGE_TYPE_INFO));
        } catch (Exception e) {
            ErrorModule.getInstance().onError(NAME, e);
        }
    }

    @Override
    public void restore(final String databaseName, final String dirBackup) {
        if (!ApplicationUtils.checkPermission(ApplicationModule.getInstance(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ApplicationUtils.runOnUiThread(() -> ApplicationUtils.grantPermisions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, SLUtil.getActivity()));
            return;
        }

        final T db = mDb.get(databaseName);
        if (db == null) {
            return;
        }

        final Class<T> klass = SafeUtils.cast(db.getClass().getSuperclass());
        final String pathDb = db.getOpenHelper().getReadableDatabase().getPath();
        if (StringUtils.isNullOrEmpty(pathDb)) {
            return;
        }

        disconnect(databaseName);

        final File fileDb = new File(pathDb);
        final String nameDb = fileDb.getName();
        final String pathBackup = dirBackup + File.separator + nameDb;
        final File fileBackup = new File(pathBackup);
        if (fileBackup.exists()) {
            try {
                if (fileDb.exists()) {
                    fileDb.delete();
                }
                if (!fileDb.exists()) {
                    Files.createParentDirs(fileDb);
                    final File dir = new File(fileDb.getParent());
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    if (dir.exists()) {
                        Files.copy(fileBackup, fileDb);
                    }
                }

                connect(klass, nameDb);

                ApplicationUtils.runOnUiThread(() -> ApplicationUtils.showToast(ApplicationModule.getInstance(), ApplicationModule.getInstance().getString(R.string.db_restored), Toast.LENGTH_LONG, BaseSnackbar.MESSAGE_TYPE_INFO));
            } catch (Exception e) {
                ErrorModule.getInstance().onError(NAME, e);
            }
        }
    }

    @Override
    public T getDb(final Class<T> klass, final String databaseName) {
        if (!isConnected(databaseName)) {
            connect(klass, databaseName);
        }
        return mDb.get(databaseName);
    }

    @Override
    public T getDb(final Class<T> klass, final String databaseName, Migration... migrations) {
        if (!isConnected(databaseName)) {
            connect(klass, databaseName, migrations);
        }
        return mDb.get(databaseName);
    }

    @Override
    public T getDb(final String databaseName) {
        if (mDb.containsKey(databaseName)) {
            return mDb.get(databaseName);
        }
        return null;
    }

    @Override
    public T getDb() {
        if (!mDb.isEmpty()) {
            return mDb.values().iterator().next();
        }
        return null;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void request(Request request) {
        if (request != null && validate()) {
            SLUtil.getRequestModule().request(this, request);
        }
    }

    @Override
    public void cancelRequests(ResponseListener listener) {
        SLUtil.getRequestModule().cancelRequests(this, listener);
    }

    @Override
    public boolean isPersistent() {
        return false;
    }

    @Override
    public void onUnRegisterModule() {
        for (String databaseName : mDb.keySet()) {
            disconnect(databaseName);
        }
    }

    @Override
    public Result<Boolean> validateExt() {
        return new Result<>(!mDb.isEmpty());
    }

    @Override
    public void onCreateDatabase(final SupportSQLiteDatabase db) {
    }

    @Override
    public void onOpenDatabase(final SupportSQLiteDatabase db) {
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (IDbProvider.class.isInstance(o)) ? 0 : 1;
    }

}
