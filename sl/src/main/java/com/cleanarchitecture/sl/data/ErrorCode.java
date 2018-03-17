package com.cleanarchitecture.sl.data;

/**
 * Created by Shishkin on 21.12.2017.
 */

public class ErrorCode {

    public static final int ERROR_NOT_GOOGLE_PLAY_SERVICES = 1;
    public static final int ERROR_NOT_GEOLOCATION_PERMISSION = 2;
    public static final int ERROR_NOT_NETWORK = 3;
    public static final int ERROR_NOT_GEOLOCATION_SERVICES = 4;
    public static final int ERROR_APPLICATION_CONTEXT = 5;
    public static final int ERROR_NOT_ENOUGH_MEMORY = 6;
    public static final int ERROR_OBJECT_IS_NULL = 7;
    public static final int ERROR_OBJECT_IS_EMPTY = 8;
    public static final int ERROR_VALIDATOR_NOT_FOUND = 9;
    public static final int ERROR_EMPTY_NAME = 10;

    private ErrorCode() {
    }
}
