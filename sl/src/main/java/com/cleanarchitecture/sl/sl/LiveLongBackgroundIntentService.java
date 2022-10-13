package com.cleanarchitecture.sl.sl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import com.cleanarchitecture.sl.handler.AutoCompleteHandler;

/**
 * Абстрактный долгоживущий сервис
 */
@SuppressWarnings("unused")
public abstract class LiveLongBackgroundIntentService extends Service
        implements AutoCompleteHandler.OnHandleEventListener<Intent>,
        AutoCompleteHandler.OnShutdownListener {

    private final String mName;

    private AutoCompleteHandler<Intent> mServiceHandler;
    private boolean mLiveLong = true;

    /**
     * Creates an LiveLongAndProsperIntentService. Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public LiveLongBackgroundIntentService(final String name) {
        super();
        mName = name;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mServiceHandler = new AutoCompleteHandler<Intent>("LiveLongBackgroundIntentService [" + mName + "]");
        mServiceHandler.setOnHandleEventListener(this);
        mServiceHandler.setOnShutdownListener(this);

    }

    /**
     * Set service should live when intents queue is empty or not.
     * Live long service can be used to listen to system or app callbacks even when
     * there are no user interface available.
     *
     * @param liveLong true if service should live when intents queue is empty, false otherwise.
     * @see #setShutdownTimeout(long)
     */
    public void setLiveLong(final boolean liveLong) {
        mLiveLong = liveLong;
    }

    /**
     * Set shutdown timeout in milliseconds when messages queue will be stopped
     * after queue is empty.
     *
     * @param shutdownTimeout The timeout in milliseconds.
     * @see #setLiveLong(boolean)
     */
    public void setShutdownTimeout(final long shutdownTimeout) {
        if (shutdownTimeout > 0) {
            setLiveLong(false);
            mServiceHandler.setShutdownTimeout(shutdownTimeout);
        }
    }

    @SuppressWarnings("deprecated")
    @Override
    public final void onStart(final Intent intent, final int startId) {
        mServiceHandler.post(intent);
    }

    /**
     * You should not override this method for your LiveLongAndProsperIntentService. Instead,
     * override {@link #onHandleIntent}, which the system calls when the
     * LiveLongAndProsperIntentService receives a start request.
     *
     * @see Service#onStartCommand
     */
    @Override
    public final int onStartCommand(final Intent intent, final int flags, final int startId) {
        onStart(intent, startId);
        return START_STICKY;
    }

    /**
     * Unless you provide binding for your service, you don't need to implement this
     * method, because the default implementation returns null.
     *
     * @see Service#onBind
     */
    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    /**
     * Callback method for async auto complete queue
     *
     * @param intent The value passed to {@link
     *               android.content.Context#startService(Intent)}.
     */
    @Override
    public final void onHandleEvent(final Intent intent) {
        if (intent != null) {
            onHandleIntent(intent);
        }
    }

    /**
     * This method is invoked on the worker thread with a request to process.
     * Only one Intent is processed at a time, but the processing happens on a
     * worker thread that runs independently from other application logic.
     * So, if this code takes a long time, it will hold up other requests to
     * the same LiveLongAndProsperIntentService, but it will not hold up anything else.
     * When all requests have been handled, the LiveLongAndProsperIntentService
     * will not stop itself, so you can use it to observer data changes.
     *
     * @param intent The value passed to {@link
     *               android.content.Context#startService(Intent)}.
     */
    @WorkerThread
    protected abstract void onHandleIntent(@NonNull final Intent intent);

    @Override
    public void onShutdown(final AutoCompleteHandler handler) {
        if (!mLiveLong) {
            stopSelf();
        }
    }

}
