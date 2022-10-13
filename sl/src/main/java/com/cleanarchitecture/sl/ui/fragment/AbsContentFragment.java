package com.cleanarchitecture.sl.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.View;

import com.cleanarchitecture.common.utils.ViewUtils;
import com.cleanarchitecture.sl.R;
import com.cleanarchitecture.sl.model.AbsModel;
import com.cleanarchitecture.sl.presenter.impl.SwipeRefreshView;
import com.cleanarchitecture.sl.sl.ModuleSubscriber;
import com.cleanarchitecture.sl.sl.SLUtil;
import com.cleanarchitecture.sl.ui.activity.OnBackPressListener;


import java.util.List;

@SuppressWarnings("unused")
public abstract class AbsContentFragment<M extends AbsModel> extends AbsFragment<M> implements
        OnBackPressListener, ModuleSubscriber, SwipeRefreshView {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipeRefreshLayout = ViewUtils.findView(view, R.id.swipeRefreshLayout);
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    @Override
    public void hideProgressBar() {
        if (validate()) {
            super.hideProgressBar();

            if (mSwipeRefreshLayout != null) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    /**
     * @return true if fragment itself or its children correctly handle back press event.
     */
    @Override
    public boolean onBackPressed() {
        boolean backPressedHandled = false;

        final FragmentManager fragmentManager = getChildFragmentManager();
        final List<Fragment> children = fragmentManager.getFragments();
        if (children != null) {
            for (final Fragment child : children) {
                if (child != null && OnBackPressListener.class.isInstance(child) && child.getUserVisibleHint()) {
                    backPressedHandled |= ((OnBackPressListener) child).onBackPressed();
                }
            }
        }
        return backPressedHandled;
    }

    /**
     * Dispatches result of activity launch to child fragments.
     */
    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        final List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null) {
            for (final Fragment child : fragments) {
                if (child != null) {
                    child.onActivityResult(requestCode, resultCode, intent);
                }
            }
        }
    }

    public void onRequestPermissions(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    }

    @Override
    public boolean isTop() {
        return false;
    }

    @Override
    public void exit() {
        SLUtil.getRoutingUnion().back();
    }

}
