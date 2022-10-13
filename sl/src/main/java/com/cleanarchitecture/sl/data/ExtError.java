package com.cleanarchitecture.sl.data;

import androidx.annotation.Nullable;

import com.cleanarchitecture.common.utils.StringUtils;

public class ExtError {
    private StringBuilder mErrorText = new StringBuilder();
    private String mSender = null;

    @Nullable
    public String getErrorText() {
        if (mErrorText.length() == 0) {
            return null;
        }
        return mErrorText.toString();
    }

    public ExtError addError(final String sender, final String error) {
        mSender = sender;
        addError(error);
        return this;
    }

    public ExtError addError(final String sender, final int errorCode) {
        mSender = sender;
        final String error = getErorText(errorCode);
        addError(error);
        return this;
    }

    private void addError(final String error) {
        if (!StringUtils.isNullOrEmpty(error)) {
            if (mErrorText.length() > 0) {
                mErrorText.append("\n");
            }
            mErrorText.append(error);
        }
    }

    public ExtError addError(final String sender, final Exception e) {
        if (e != null) {
            mSender = sender;
            addError(e.getMessage());
        }
        return this;
    }

    public boolean hasError() {
        return mErrorText.length() > 0;
    }

    public String getSender() {
        return mSender;
    }

    public ExtError setSender(final String sender) {
        mSender = sender;
        return this;
    }

    public static String getErorText(final int errorCode) {
        if (errorCode == 0) return null;

        if (errorCode == ErrorCode.ERROR_NOT_GEOLOCATION_PERMISSION)
            return "Код ошибки:" + errorCode + ". У приложения нет прав на геолокацию";

        if (errorCode == ErrorCode.ERROR_NOT_GOOGLE_PLAY_SERVICES)
            return "Код ошибки:" + errorCode + ". На смартфоне не установлены Google Play Services";

        if (errorCode == ErrorCode.ERROR_NOT_NETWORK)
            return "Код ошибки:" + errorCode + ". Отсутствует сеть";

        if (errorCode == ErrorCode.ERROR_NOT_GEOLOCATION_SERVICES)
            return "Код ошибки:" + errorCode + ". Служба геолокации не включена";

        if (errorCode == ErrorCode.ERROR_APPLICATION_CONTEXT)
            return "Код ошибки:" + errorCode + ". Не могу получить контекст приложения";

        if (errorCode == ErrorCode.ERROR_NOT_ENOUGH_MEMORY)
            return "Код ошибки:" + errorCode + ". Не достаточно памяти приложению";

        if (errorCode == ErrorCode.ERROR_OBJECT_IS_NULL)
            return "Код ошибки:" + errorCode + ". Объект равен null";

        if (errorCode == ErrorCode.ERROR_OBJECT_IS_EMPTY)
            return "Код ошибки:" + errorCode + ". Объект пуст";

        if (errorCode == ErrorCode.ERROR_VALIDATOR_NOT_FOUND)
            return "Код ошибки:" + errorCode + ". Валидатор не найден";

        if (errorCode == ErrorCode.ERROR_EMPTY_NAME)
            return "Код ошибки:" + errorCode + ". Не указано имя объекта";

        return "Код ошибки:" + errorCode + ". Неизвестная ошибка";
    }
}
