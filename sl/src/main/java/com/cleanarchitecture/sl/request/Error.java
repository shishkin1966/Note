package com.cleanarchitecture.sl.request;

/**
 * Created by MerkurevVV on 13.09.2016.
 */
public class Error {
    private String mMessage;

    public Error() {
    }

    public Error(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }
}
