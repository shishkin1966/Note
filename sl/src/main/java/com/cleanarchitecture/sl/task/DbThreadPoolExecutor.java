package com.cleanarchitecture.sl.task;

import com.cleanarchitecture.sl.request.AbsRequest;
import com.cleanarchitecture.sl.request.Request;
import com.cleanarchitecture.sl.request.ResponseListener;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public class DbThreadPoolExecutor implements IExecutor {

    public static final String NAME = DbThreadPoolExecutor.class.getName();
    private static int QUEUE_CAPACITY = 1024;
    private int mThreadCount = 4;
    private int mMaxThreadCount = 4;
    private long mKeepAliveTime = 10; // 10 мин
    private TimeUnit mUnit = TimeUnit.MINUTES;
    private RequestThreadPoolExecutor mExecutor;
    private static volatile DbThreadPoolExecutor sInstance;

    public static DbThreadPoolExecutor getInstance() {
        if (sInstance == null) {
            synchronized (DbThreadPoolExecutor.class) {
                if (sInstance == null) {
                    sInstance = new DbThreadPoolExecutor();
                }
            }
        }
        return sInstance;
    }

    private DbThreadPoolExecutor() {
        final BlockingQueue queue = new PriorityBlockingQueue<AbsRequest>(QUEUE_CAPACITY);
        mExecutor = new RequestThreadPoolExecutor(mThreadCount, mMaxThreadCount, mKeepAliveTime, mUnit, queue);
    }

    public synchronized void execute(final Request request) {
        mExecutor.addRequest(request);
    }

    public void shutdown() {
        mExecutor.shutdown();
    }

    @Override
    public void clear() {
    }

    public synchronized void cancelRequests(ResponseListener listener) {
        mExecutor.cancelRequests(listener);
    }

    public synchronized boolean isShutdown() {
        return mExecutor.isShutdown();
    }

    @Override
    public void processing(Object sender, Object object) {
        execute((Request) object);
    }
}
