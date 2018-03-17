package com.cleanarchitecture.sl.request;

import java.lang.ref.WeakReference;

/**
 * Created by Shishkin on 04.12.2017.
 */

public abstract class AbsResultRequest<T> extends AbsRequest implements ResultRequest {

    private WeakReference<ResponseListener> mListener;

    public AbsResultRequest(final ResponseListener listener) {
        mListener = new WeakReference<>(listener);
    }

    @Override
    public ResponseListener getListener() {
        if (mListener != null) {
            return mListener.get();
        }
        return null;
    }

    @Override
    public boolean validate() {
        return (mListener != null && mListener.get() != null && mListener.get().validate() && !isCancelled());
    }

}
