package com.cleanarchitecture.sl.sl;

import android.content.Intent;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;

import com.cleanarchitecture.common.utils.StringUtils;
import com.cleanarchitecture.sl.event.navigation.ShowFragmentEvent;
import com.cleanarchitecture.sl.event.navigation.StartActivityEvent;
import com.cleanarchitecture.sl.event.navigation.StartActivityForResultEvent;
import com.cleanarchitecture.sl.event.navigation.StartChooseActivityEvent;
import com.cleanarchitecture.sl.ui.activity.AbsActivity;

/**
 * Объединение, отвечающее за навигацию в приложении
 */
public class RoutingUnion extends AbsUnion<Router> implements IRoutingUnion {

    public static final String NAME = RoutingUnion.class.getName();

    public RoutingUnion() {
        super();
    }

    @Override
    public <F> F getFragment(Class<F> cls, int id) {
        final AbsActivity activity = getActivity();
        if (activity != null) {
            return BackStack.getFragment(activity, cls, id);
        }
        return null;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isBackStackEmpty() {
        final AbsActivity activity = getActivity();
        if (activity != null) {
            return BackStack.isBackStackEmpty(activity);
        }
        return true;
    }

    @Override
    public int getBackStackEntryCount() {
        final AbsActivity activity = getActivity();
        if (activity != null) {
            BackStack.getBackStackEntryCount(activity);
        }
        return 0;
    }

    @Override
    public void switchToTopFragment() {
        final AbsActivity activity = getActivity();
        if (activity != null) {
            BackStack.switchToTopFragment(activity);
        }
    }

    @Override
    public boolean hasTop() {
        final AbsActivity activity = getActivity();
        if (activity != null) {
            return BackStack.hasTopFragment(activity);
        }
        return false;
    }

    @Override
    public <C> C getActivity() {
        final Router subscriber = getCurrentSubscriber();
        if (subscriber != null && AbsActivity.class.isInstance(subscriber)) {
            return (C) subscriber;
        }
        return null;
    }

    @Override
    public LayoutInflater getInflater() {
        final AbsActivity activity = getActivity();
        if (activity != null) {
            return LayoutInflater.from(activity);
        }
        return null;
    }

    @Override
    public <C> C getActivity(final String name) {
        return getActivity(name, false);
    }

    @Override
    public <C> C getActivity(final String name, final boolean validate) {
        final Router subscriber = getSubscriber(name);
        if (subscriber != null && AbsActivity.class.isInstance(subscriber)) {
            if (!validate || (validate && subscriber.validate())) {
                return (C) subscriber;
            }
        }
        return null;
    }

    @Override
    public void startActivity(final StartActivityEvent event) {
        if (event != null) {
            final AbsActivity activity = getActivity();
            if (activity != null) {
                activity.startActivity(event.getIntent());
            }
        }
    }

    @Override
    public void startChooseActivity(final StartChooseActivityEvent event) {
        if (event != null) {
            final AbsActivity activity = getActivity();
            if (activity != null) {
                activity.startActivity(Intent.createChooser(event.getIntent(), event.getTitle()));
            }
        }
    }

    @Override
    public void startActivityForResult(final StartActivityForResultEvent event) {
        if (event != null) {
            final AbsActivity activity = getActivity();
            if (activity != null) {
                activity.startActivityForResult(event.getIntent(), event.getRequestCode());
            }
        }
    }

    @Override
    public void showFragment(ShowFragmentEvent event) {
        final AbsActivity activity = getActivity();
        if (activity != null) {
            BackStack.showFragment(activity, event.getIdRes(), event.getFragment(), event.isAddToBackStack(), event.isClearBackStack(), event.isAnimate(), event.isAllowingStateLoss());
        }
    }

    @Override
    public void switchToFragment(String name) {
        if (StringUtils.isNullOrEmpty(name)) return;

        final Router subscriber = getCurrentSubscriber();
        if (subscriber != null) {
            subscriber.switchToFragment(name);
        }
    }

    @Override
    public void back() {
        final Router subscriber = getCurrentSubscriber();
        if (subscriber != null) {
            subscriber.onBackPressed();
        }
    }

    @Override
    public void showActivity(AbsActivity activity) {
        if (activity == null) return;

        startActivity(new StartActivityEvent(new Intent(ApplicationModule.getInstance(), activity.getClass())));
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (IRoutingUnion.class.isInstance(o)) ? 0 : 1;
    }
}
