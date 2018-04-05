package com.cleanarchitecture.sl.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.View;

import com.cleanarchitecture.common.utils.StringUtils;
import com.cleanarchitecture.common.utils.ViewUtils;
import com.cleanarchitecture.sl.R;
import com.cleanarchitecture.sl.data.Result;
import com.cleanarchitecture.sl.model.AbsModel;
import com.cleanarchitecture.sl.model.Model;
import com.cleanarchitecture.sl.model.ModelView;
import com.cleanarchitecture.sl.sl.IActivityUnion;
import com.cleanarchitecture.sl.sl.MailSubscriber;
import com.cleanarchitecture.sl.sl.MailUnion;
import com.cleanarchitecture.sl.sl.ModuleSubscriber;
import com.cleanarchitecture.sl.sl.SLUtil;
import com.cleanarchitecture.sl.state.StateObservable;
import com.cleanarchitecture.sl.state.Stateable;
import com.cleanarchitecture.sl.state.ViewStateObserver;
import com.cleanarchitecture.sl.ui.activity.IActivity;


import java.util.List;

@SuppressWarnings("unused")
public abstract class AbsFragment<M extends AbsModel> extends Fragment
        implements IFragment, MailSubscriber, ModuleSubscriber, ModelView {

    private StateObservable mStateObservable = new StateObservable(ViewStateObserver.STATE_CREATE);
    private M mModel;

    @Override
    public M getModel() {
        return mModel;
    }

    @Override
    public void setModel(Model model) {
        if (!validate()) return;

        mModel = (M) model;
    }

    @Override
    public <V extends View> V findView(@IdRes final int id) {
        final View root = getView();
        if (root != null) {
            return ViewUtils.findView(root, id);
        }
        return null;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mStateObservable.setState(ViewStateObserver.STATE_READY);

        SLUtil.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        mStateObservable.setState(ViewStateObserver.STATE_PAUSE);
    }

    @Override
    public void onResume() {
        super.onResume();

        mStateObservable.setState(ViewStateObserver.STATE_RESUME);

        SLUtil.readMail(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mStateObservable.setState(ViewStateObserver.STATE_ACTIVITY_CREATED);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mStateObservable.setState(ViewStateObserver.STATE_DESTROY);
        mStateObservable.clear();

        SLUtil.unregister(this);
    }

    @Override
    public List<String> getModuleSubscription() {
        return StringUtils.arrayToList(MailUnion.NAME);
    }

    @Override
    public IActivity getActivitySubscriber() {
        if (validate()) {
            final Activity activity = getActivity();
            if (activity != null && IActivity.class.isInstance(activity)) {
                return (IActivity) activity;
            }
            final IActivityUnion union = SLUtil.getActivityUnion();
            if (union != null) {
                return union.getCurrentSubscriber();
            }
        }
        return null;
    }

    @Override
    public int getState() {
        return mStateObservable.getState();
    }

    @Override
    public void setState(int state) {
    }

    @Override
    public void exit() {
    }

    @Override
    public Result<Boolean> validateExt() {
        return new Result<>(getState() != ViewStateObserver.STATE_DESTROY);
    }

    @Override
    public boolean validate() {
        return validateExt().getData();
    }

    @Override
    public void showProgressBar() {
        if (validate()) {
            final View view = findView(R.id.progressBar);
            if (view != null) {
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void hideProgressBar() {
        if (validate()) {
            final View view = findView(R.id.progressBar);
            if (view != null) {
                view.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void showModalProgressBar() {
        if (validate()) {
            final View view = findView(R.id.modalBar);
            if (view != null) {
                view.setVisibility(View.VISIBLE);
                ViewUtils.enableDisableTouchView(getRootView(), false);
            }
        }
    }

    @Override
    public void hideModalProgressBar() {
        if (validate()) {
            final View view = findView(R.id.modalBar);
            if (view != null) {
                view.setVisibility(View.INVISIBLE);
                ViewUtils.enableDisableTouchView(getRootView(), true);
            }
        }
    }

    @Override
    public void showProgressDialog() {
        final IActivity activity = getActivitySubscriber();
        if (activity != null) {
            activity.showProgressDialog();
        }
    }

    @Override
    public void hideProgressDialog() {
        final IActivity activity = getActivitySubscriber();
        if (activity != null) {
            activity.hideProgressDialog();
        }
    }

    @Override
    public View getRootView() {
        final View view = findView(R.id.root);
        if (view != null) {
            return view;
        }
        return getView();
    }

    @Override
    public void addStateObserver(final Stateable stateable) {
        mStateObservable.addObserver(stateable);
    }

    @Override
    public int getAccentColor() {
        if (!isAdded()) return ViewUtils.getColor(SLUtil.getContext(), R.color.colorAccent);

        final TypedValue typedValue = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        return typedValue.data;
    }

    @Override
    public int getPrimaryColor() {
        if (!isAdded()) return ViewUtils.getColor(SLUtil.getContext(), R.color.colorPrimary);

        final TypedValue typedValue = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

}
