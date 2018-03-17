package com.cleanarchitecture.sl.request;

/**
 * Created by Shishkin on 19.12.2017.
 */

public class BaseRequest extends AbsRequest {

    @Override
    public String getName() {
        return BaseRequest.class.getName();
    }

    @Override
    public boolean isDistinct() {
        return false;
    }

    @Override
    public void run() {
    }
}
