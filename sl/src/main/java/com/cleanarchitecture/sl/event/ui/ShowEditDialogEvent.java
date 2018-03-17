package com.cleanarchitecture.sl.event.ui;

/**
 * Событие - выполнить команду "показать Edit диалог"
 */
public class ShowEditDialogEvent extends ShowDialogEvent {

    private String mEditText;
    private String mHint;
    private int mInputType;

    public ShowEditDialogEvent(final int id, final String listener) {
        super(id, listener);
    }

    public ShowEditDialogEvent(final int id, final String listener, final String title, final String message, final String editText, final String hint, final int input_type, final int button_positive, final int button_negative, final boolean cancelable) {
        super(id, listener, title, message);

        mEditText = editText;
        mHint = hint;
        mInputType = input_type;

        setPositiveButton(button_positive);
        setNegativeButton(button_negative);
        setCancelable(cancelable);
    }


    public String getEditText() {
        return mEditText;
    }

    public String getHint() {
        return mHint;
    }

    public int getInputType() {
        return mInputType;
    }

    public ShowEditDialogEvent setEditText(String editText) {
        mEditText = editText;
        return this;
    }

    public ShowEditDialogEvent setHint(String hint) {
        mHint = hint;
        return this;
    }

    public ShowEditDialogEvent setInputType(int inputType) {
        mInputType = inputType;
        return this;
    }

}
