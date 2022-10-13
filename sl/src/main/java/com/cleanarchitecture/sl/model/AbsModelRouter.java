package com.cleanarchitecture.sl.model;

import android.app.Activity;
import androidx.fragment.app.Fragment;

import com.cleanarchitecture.sl.event.navigation.ShowFragmentEvent;
import com.cleanarchitecture.sl.sl.ApplicationModule;
import com.cleanarchitecture.sl.sl.ErrorModule;
import com.cleanarchitecture.sl.sl.SLUtil;
import com.cleanarchitecture.sl.ui.activity.AbsContentActivity;
import com.cleanarchitecture.sl.ui.fragment.AbsFragment;

public abstract class AbsModelRouter implements ModelRouter {

    private static final String NAME = AbsModelRouter.class.getName();

    private AbsModel mModel;

    public AbsModelRouter(AbsModel model) {
        mModel = model;
    }

    @Override
    public void finishApplication() {
        ApplicationModule.getInstance().finish();
    }

    @Override
    public void switchToTop() {
        final ModelView view = getModel().getView();
        if (AbsContentActivity.class.isInstance(view)) {
            final AbsContentActivity activity = (AbsContentActivity) view;
            activity.switchToTopFragment();
            return;
        } else if (AbsFragment.class.isInstance(view)) {
            final Activity activity = ((AbsFragment) view).getActivity();
            if (activity != null && AbsContentActivity.class.isInstance(activity)) {
                ((AbsContentActivity) activity).switchToTopFragment();
                return;
            }
        }
        if (SLUtil.getRoutingUnion().hasTop()) {
            SLUtil.getRoutingUnion().switchToTopFragment();
        } else {
            showMainFragment();
        }
    }

    public abstract void showMainFragment();

    public AbsModel getModel() {
        return mModel;
    }

    @Override
    public void showFragment(final Fragment fragment) {
        showFragment(fragment, true, false, true, false);
    }

    @Override
    public void showFragment(final Fragment fragment, final boolean allowingStateLoss) {
        showFragment(fragment, true, false, true, allowingStateLoss);
    }

    @Override
    public void showFragment(final Fragment fragment, final boolean addToBackStack,
                             final boolean clearBackStack,
                             final boolean animate, final boolean allowingStateLoss) {
        final ModelView view = getModel().getView();
        if (AbsContentActivity.class.isInstance(view)) {
            final AbsContentActivity activity = (AbsContentActivity) view;
            activity.showFragment(fragment, addToBackStack, clearBackStack, animate, allowingStateLoss);
            return;
        } else if (AbsFragment.class.isInstance(view)) {
            final Activity activity = ((AbsFragment) view).getActivity();
            if (activity != null && AbsContentActivity.class.isInstance(activity)) {
                ((AbsContentActivity) activity).showFragment(fragment, addToBackStack, clearBackStack, animate, allowingStateLoss);
                return;
            } else {
                ErrorModule.getInstance().onError(NAME, "Fragment is not attached to activity", false);
            }
        } else {
            ErrorModule.getInstance().onError(NAME, "Object is not AbsContentActivity and AbsFragment", false);
        }
        SLUtil.getRoutingUnion().showFragment(new ShowFragmentEvent(fragment, allowingStateLoss, addToBackStack, clearBackStack, animate));
    }
}
