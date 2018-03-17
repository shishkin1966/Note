package com.cleanarchitecture.sl.ui;

public interface SnackBarView {
    void showSnackbar(String message, int duration, int type);

    void hideSnackbar();
}
