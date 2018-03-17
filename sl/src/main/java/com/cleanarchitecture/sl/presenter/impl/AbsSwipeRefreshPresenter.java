package com.cleanarchitecture.sl.presenter.impl;

import android.support.v4.widget.SwipeRefreshLayout;

import com.cleanarchitecture.sl.R;
import com.cleanarchitecture.sl.presenter.AbsPresenter;

public abstract class AbsSwipeRefreshPresenter extends AbsPresenter {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SwipeRefreshView mView;

    public AbsSwipeRefreshPresenter(final SwipeRefreshView view) {
        if (view != null && view.getSwipeRefreshLayout() != null) {
            mView = view;
            mSwipeRefreshLayout = mView.getSwipeRefreshLayout();
            mSwipeRefreshLayout.setColorSchemeResources(R.color.blue);
            mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.gray_light);

            mSwipeRefreshLayout.setOnRefreshListener(() -> {
                if (validate()) {
                    refreshData();
                }
            });
        }
    }

    public abstract void refreshData();

    @Override
    public boolean isRegister() {
        return false;
    }

}
