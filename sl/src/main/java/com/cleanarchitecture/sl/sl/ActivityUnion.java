package com.cleanarchitecture.sl.sl;

import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.cleanarchitecture.common.ui.BaseSnackbar;
import com.cleanarchitecture.common.utils.ApplicationUtils;
import com.cleanarchitecture.common.utils.Constant;
import com.cleanarchitecture.common.utils.SafeUtils;
import com.cleanarchitecture.common.utils.StringUtils;
import com.cleanarchitecture.sl.R;
import com.cleanarchitecture.sl.data.ErrorCode;
import com.cleanarchitecture.sl.event.ui.DialogResultEvent;
import com.cleanarchitecture.sl.event.ui.OnSnackBarClickEvent;
import com.cleanarchitecture.sl.event.ui.ShowDialogEvent;
import com.cleanarchitecture.sl.event.ui.ShowEditDialogEvent;
import com.cleanarchitecture.sl.event.ui.ShowErrorMessageEvent;
import com.cleanarchitecture.sl.event.ui.ShowKeyboardEvent;
import com.cleanarchitecture.sl.event.ui.ShowListDialogEvent;
import com.cleanarchitecture.sl.event.ui.ShowMessageEvent;
import com.cleanarchitecture.sl.event.ui.ShowProgressDialogEvent;
import com.cleanarchitecture.sl.state.ViewStateObserver;
import com.cleanarchitecture.sl.ui.activity.AbsActivity;
import com.cleanarchitecture.sl.ui.activity.AbsContentActivity;
import com.cleanarchitecture.sl.ui.activity.IActivity;
import com.cleanarchitecture.sl.ui.dialog.DialogResultListener;
import com.cleanarchitecture.sl.ui.dialog.MaterialDialogExt;
import com.cleanarchitecture.sl.ui.fragment.AbsContentFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import es.dmoral.toasty.Toasty;

/**
 * Объединение activities
 */
@SuppressWarnings("unused")
public class ActivityUnion extends AbsUnion<IActivity> implements IActivityUnion, DialogResultListener {

    public static final String NAME = ActivityUnion.class.getName();
    private List<WeakReference<IActivity>> mActivities = Collections.synchronizedList(new ArrayList<>());

    public ActivityUnion() {
        super();
    }

    @Override
    public void register(final IActivity subscriber) {
        super.register(subscriber);

        for (int i = mActivities.size() - 1; i >= 0; i--) {
            if (mActivities.get(i) == null) {
                mActivities.remove(i);
                continue;
            }
            if (mActivities.get(i).get() == null) {
                mActivities.remove(i);
                continue;
            }

            if (subscriber != null) {
                if (mActivities.get(i).get().getName().equals(subscriber.getName())) {
                    mActivities.get(i).get().exit();
                    mActivities.remove(i);
                }
            }
        }

        if (subscriber != null) {
            mActivities.add(new WeakReference<>(subscriber));
        }
    }

    @Override
    public void unregister(final IActivity subscriber) {
        super.unregister(subscriber);

        for (int i = mActivities.size() - 1; i >= 0; i--) {
            if (mActivities.get(i) == null) {
                mActivities.remove(i);
                continue;
            }
            if (mActivities.get(i).get() == null) {
                mActivities.remove(i);
                continue;
            }

            if (subscriber != null) {
                if (mActivities.get(i).get().equals(subscriber)) {
                    mActivities.remove(i);
                }
            }
        }
    }

    @Override
    public boolean checkPermission(String permission) {
        if (ApplicationUtils.hasMarshmallow()) {
            final IActivity subscriber = getCurrentSubscriber();
            if (subscriber != null && subscriber.validate() && (subscriber.getActivity().getState() == ViewStateObserver.STATE_RESUME || subscriber.getActivity().getState() == ViewStateObserver.STATE_PAUSE)) {
                if (ActivityCompat.checkSelfPermission(subscriber.getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void checkGooglePlayServices() {
        final IActivity subscriber = getCurrentSubscriber();
        if (subscriber != null && subscriber.validate()) {
            if (subscriber.getActivity().getState() == ViewStateObserver.STATE_RESUME || subscriber.getActivity().getState() == ViewStateObserver.STATE_PAUSE) {
                final GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
                final int result = googleAPI.isGooglePlayServicesAvailable(subscriber.getActivity());
                if (result != ConnectionResult.SUCCESS) {
                    if (googleAPI.isUserResolvableError(result)) {
                        final Dialog dialog = googleAPI.getErrorDialog(subscriber.getActivity(), result, Constant.REQUEST_GOOGLE_PLAY_SERVICES);
                        dialog.setOnCancelListener(dialogInterface -> subscriber.getActivity().finish());
                        dialog.show();
                    }
                }
            }
        }
    }

    @Override
    public void grantPermission(String permission, String helpMessage) {
        if (ApplicationUtils.hasMarshmallow()) {
            final IActivity subscriber = getCurrentSubscriber();
            if (subscriber != null && subscriber.validate()) {
                if (subscriber.getActivity().getState() == ViewStateObserver.STATE_RESUME || subscriber.getActivity().getState() == ViewStateObserver.STATE_PAUSE) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(subscriber.getActivity(), permission)) {
                        showDialog(new ShowDialogEvent(R.id.dialog_request_permissions, this.getName(), null, helpMessage, R.string.setting, R.string.cancel, false));
                    } else {
                        ActivityCompat.requestPermissions(subscriber.getActivity(), new String[]{permission}, Constant.REQUEST_PERMISSIONS);
                    }
                }
            }
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void showSnackbar(ShowMessageEvent event) {
        final IActivity subscriber = getCurrentSubscriber();
        View view = null;
        if (subscriber != null && subscriber.validate()) {
            if (AbsContentActivity.class.isInstance(subscriber)) {
                final AbsContentFragment fragment = SafeUtils.cast(((AbsContentActivity) subscriber).getContentFragment(AbsContentFragment.class));
                if (fragment != null) {
                    view = fragment.getRootView();
                }
            }
            if (view == null) {
                view = subscriber.getRootView();
            }
            if (view != null) {
                final String action = event.getAction();
                if (StringUtils.isNullOrEmpty(action)) {
                    BaseSnackbar.make(view, event.getMessage(), event
                            .getDuration(), event.getType())
                            .show();
                } else {
                    BaseSnackbar.make(view, event.getMessage(), event.getDuration(), event.getType())
                            .setAction(action, this::onSnackbarClick)
                            .show();
                }
            }
        }
    }

    @Override
    public void showErrorMessage(ShowErrorMessageEvent event) {
        if (event.getErrorText().contains(":" + ErrorCode.ERROR_NOT_NETWORK + ".")) return;

        final IActivity subscriber = getCurrentSubscriber();
        if (subscriber != null && subscriber.validate()) {
            if (subscriber.getActivity().getState() == ViewStateObserver.STATE_RESUME || subscriber.getActivity().getState() == ViewStateObserver.STATE_PAUSE) {
                new MaterialDialogExt(subscriber.getActivity(), this.getName(), event.getId(), subscriber.getActivity().getResources().getString(R.string.warning), event.getErrorText(), R.string.ok_upper, MaterialDialogExt.NO_BUTTON, false).show();
            }
        }
    }

    private void onSnackbarClick(final View view) {
        String action = null;
        if (AppCompatButton.class.isInstance(view)) {
            action = ((AppCompatButton) view).getText().toString();
        } else if (Button.class.isInstance(view)) {
            action = ((Button) view).getText().toString();
        }
        if (!StringUtils.isNullOrEmpty(action)) {
            SLUtil.getUseCasesUnion().onSnackBarClick(new OnSnackBarClickEvent(action));
        }
    }

    @Override
    public void showToast(ShowMessageEvent event) {
        final IActivity activity = getCurrentSubscriber();
        if (activity == null || !activity.validate()) {
            ErrorModule.getInstance().onError(NAME + ":showToast", "Подписчик не найден или он не работоспособный", false);
            return;
        }

        final int type = event.getType();
        final int duration = event.getDuration();
        final String message = event.getMessage();

        switch (type) {
            case BaseSnackbar.MESSAGE_TYPE_INFO:
                Toasty.info(activity.getActivity(), message, duration).show();
                break;

            case BaseSnackbar.MESSAGE_TYPE_ERROR:
                Toasty.error(activity.getActivity(), message, duration).show();
                break;

            case BaseSnackbar.MESSAGE_TYPE_WARNING:
                Toasty.warning(activity.getActivity(), message, duration).show();
                break;

            case BaseSnackbar.MESSAGE_TYPE_SUCCESS:
                Toasty.success(activity.getActivity(), message, duration).show();
                break;

            default:
                Toasty.info(activity.getActivity(), message, duration).show();
                break;

        }
    }

    @Override
    public void hideKeyboard() {
        final IActivity subscriber = getCurrentSubscriber();
        if (subscriber != null) {
            subscriber.hideKeyboard();
        }
    }

    @Override
    public void showKeyboard(ShowKeyboardEvent event) {
        final IActivity subscriber = getCurrentSubscriber();
        if (subscriber != null && subscriber.validate()) {
            subscriber.showKeyboard(event.getView());
        }
    }

    @Override
    public void showProgressBar() {
        final IActivity subscriber = getCurrentSubscriber();
        if (subscriber != null && subscriber.validate()) {
            if (AbsContentActivity.class.isInstance(subscriber)) {
                final AbsContentActivity activity = (AbsContentActivity) subscriber;
                final AbsContentFragment fragment = SafeUtils.cast(activity.getContentFragment(AbsContentFragment.class));
                if (fragment != null) {
                    fragment.showProgressBar();
                }
            }
        }
    }

    @Override
    public void hideProgressBar() {
        final IActivity subscriber = getCurrentSubscriber();
        if (subscriber != null) {
            if (AbsContentActivity.class.isInstance(subscriber)) {
                final AbsContentActivity activity = (AbsContentActivity) subscriber;
                final AbsContentFragment fragment = SafeUtils.cast(activity.getContentFragment(AbsContentFragment.class));
                if (fragment != null) {
                    fragment.hideProgressBar();
                }
            }
        }
    }

    @Override
    public void showProgressDialog(final ShowProgressDialogEvent event) {
        final IActivity subscriber = getCurrentSubscriber();
        if (subscriber != null && subscriber.validate()) {
            subscriber.showProgressDialog(event.getTitle());
        }
    }

    @Override
    public void hideProgressDialog() {
        final IActivity subscriber = getCurrentSubscriber();
        if (subscriber != null) {
            subscriber.hideProgressDialog();
        }
    }

    @Override
    public void showListDialog(ShowListDialogEvent event) {
        final IActivity subscriber = getCurrentSubscriber();
        if (subscriber != null && subscriber.validate()) {
            if (subscriber.getActivity().getState() == ViewStateObserver.STATE_RESUME || subscriber.getActivity().getState() == ViewStateObserver.STATE_PAUSE) {
                new MaterialDialogExt(subscriber.getActivity(), event.getListener(), event.getId(),
                        event.getTitle(), event.getMessage(), event.getList(), event.getSelected(), event.isMultiselect(), event.getButtonPositive(),
                        event.getButtonNegative(), event.isCancelable()).show();
            }
        }
    }

    @Override
    public void showEditDialog(ShowEditDialogEvent event) {
        final IActivity subscriber = getCurrentSubscriber();
        if (subscriber != null && subscriber.validate()) {
            if (subscriber.getActivity().getState() == ViewStateObserver.STATE_RESUME || subscriber.getActivity().getState() == ViewStateObserver.STATE_PAUSE) {
                new MaterialDialogExt(subscriber.getActivity(), event.getListener(), event.getId(), event.getTitle(), event.getMessage(), event.getEditText(), event.getHint(), event.getInputType(), event.getButtonPositive(),
                        event.getButtonNegative(), event.isCancelable()).show();
            }
        }
    }

    @Override
    public void showDialog(ShowDialogEvent event) {
        final IActivity subscriber = getCurrentSubscriber();
        if (subscriber != null && subscriber.validate()) {
            if (subscriber.getActivity().getState() == ViewStateObserver.STATE_RESUME || subscriber.getActivity().getState() == ViewStateObserver.STATE_PAUSE) {
                new MaterialDialogExt(subscriber.getActivity(), event.getListener(), event.getId(), event.getTitle(), event.getMessage(), event.getButtonPositive(), event.getButtonNegative(), event.isCancelable()).show();
            }
        }
    }

    @Override
    public void onDialogResult(DialogResultEvent event) {
        if (event.getId() == R.id.dialog_show_lock_screen_setting) {
            if (event.getResult().getString(MaterialDialogExt.BUTTON).equals(MaterialDialogExt.POSITIVE)) {
                final IActivity subscriber = getCurrentSubscriber();
                if (subscriber != null && subscriber.validate() && (subscriber.getActivity().getState() == ViewStateObserver.STATE_RESUME || subscriber.getActivity().getState() == ViewStateObserver.STATE_PAUSE)) {
                    final Intent intent = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
                    ((AbsActivity) subscriber).startActivity(intent);
                }
            }
        } else if (event.getId() == R.id.dialog_enable_location) {
            if (event.getResult().getString(MaterialDialogExt.BUTTON).equals(MaterialDialogExt.POSITIVE)) {
                final IActivity subscriber = getCurrentSubscriber();
                if (subscriber != null && subscriber.validate() && (subscriber.getActivity().getState() == ViewStateObserver.STATE_RESUME || subscriber.getActivity().getState() == ViewStateObserver.STATE_PAUSE)) {
                    final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    ((AbsActivity) subscriber).startActivity(intent);
                }
            }
        } else {
            final IActivity subscriber = getCurrentSubscriber();
            if (subscriber != null && subscriber.validate() && (subscriber.getActivity().getState() == ViewStateObserver.STATE_RESUME || subscriber.getActivity().getState() == ViewStateObserver.STATE_PAUSE)) {
                subscriber.onDialogResult(event);
            }
        }
    }

    @Override
    public void onFinishApplication() {
        for (WeakReference<IActivity> ref : mActivities) {
            if (ref != null && ref.get() != null) {
                ref.get().exit();
            }
        }
    }

    @Override
    public void onUnRegisterLastSubscriber() {
        if (ApplicationModule.getInstance().isFinished()) {
            if (ApplicationModule.getInstance().isKillOnFinish()) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
    }

    @Override
    public void showLockScreenSetting() {
        final Context context = ApplicationModule.getInstance();
        if (context != null) {
            showDialog(new ShowDialogEvent(R.id.dialog_show_lock_screen_setting, this.getName(), null, context.getString(R.string.error_lock_keystore), R.string.goto_lock_screen_setting, R.string.exit, false));
        }
    }

    @Override
    public void checkLocationService() {
        final Context context = SLUtil.getContext();
        if (context == null) return;

        final IActivity subscriber = getCurrentSubscriber();
        if (subscriber != null && subscriber.validate()) {
            if (subscriber.getActivity().getState() == ViewStateObserver.STATE_RESUME || subscriber.getActivity().getState() == ViewStateObserver.STATE_PAUSE) {
                if (!ApplicationUtils.isLocationEnabled(SLUtil.getContext())) {
                    showDialog(new ShowDialogEvent(R.id.dialog_enable_location, this.getName(), context.getString(R.string.warning), context.getString(R.string.enable_location), R.string.setting, R.string.exit));
                }
            }
        }
    }

    @Override
    public LayoutInflater getInflater() {
        final IActivity subscriber = getCurrentSubscriber();
        if (subscriber != null && subscriber.validate()) {
            return LayoutInflater.from((AbsActivity) subscriber);
        }
        return null;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (IActivityUnion.class.isInstance(o)) ? 0 : 1;
    }

    @Override
    public List<String> getModuleSubscription() {
        return StringUtils.arrayToList(MailUnion.NAME);
    }

    @Override
    public int getState() {
        if (validate()) {
            return ViewStateObserver.STATE_RESUME;
        } else {
            return ViewStateObserver.STATE_DESTROY;
        }
    }

    @Override
    public void setState(int state) {
    }
}
