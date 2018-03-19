package com.cleanarchitecture.sl.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cleanarchitecture.common.ui.KeyboardRunnable;
import com.cleanarchitecture.common.utils.ApplicationUtils;
import com.cleanarchitecture.common.utils.PreferencesUtils;
import com.cleanarchitecture.common.utils.StringUtils;
import com.cleanarchitecture.common.utils.ViewUtils;
import com.cleanarchitecture.sl.R;
import com.cleanarchitecture.sl.data.Result;
import com.cleanarchitecture.sl.event.ui.DialogResultEvent;
import com.cleanarchitecture.sl.model.Model;
import com.cleanarchitecture.sl.model.ModelView;
import com.cleanarchitecture.sl.sl.ActivityUnion;
import com.cleanarchitecture.sl.sl.BackStack;
import com.cleanarchitecture.sl.sl.MailSubscriber;
import com.cleanarchitecture.sl.sl.MailUnion;
import com.cleanarchitecture.sl.sl.SLUtil;
import com.cleanarchitecture.sl.state.StateObservable;
import com.cleanarchitecture.sl.state.Stateable;
import com.cleanarchitecture.sl.state.ViewStateObserver;
import com.cleanarchitecture.sl.ui.dialog.MaterialDialogExt;


import java.lang.ref.WeakReference;
import java.util.List;

public abstract class AbsActivity<M extends Model> extends AppCompatActivity
        implements IActivity, MailSubscriber, ModelView {

    private StateObservable mStateObservable = new StateObservable(ViewStateObserver.STATE_CREATE);
    private WeakReference<MaterialDialog> mMaterialDialogWeakReference;
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
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStateObservable.setState(ViewStateObserver.STATE_CREATE);

        SLUtil.register(this);
    }

    @Override
    public <V extends View> V findView(@IdRes final int id) {
        return ViewUtils.findView(this, id);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mStateObservable.setState(ViewStateObserver.STATE_READY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        hideProgressDialog();

        mStateObservable.setState(ViewStateObserver.STATE_DESTROY);
        mStateObservable.clear();

        SLUtil.unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mStateObservable.setState(ViewStateObserver.STATE_RESUME);

        SLUtil.setCurrentSubscriber(this);

        SLUtil.readMail(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mStateObservable.setState(ViewStateObserver.STATE_PAUSE);
    }

    /**
     * Called to process touch screen events.  You can override this to
     * intercept all touch screen events before they are dispatched to the
     * window.  Be sure to call this implementation for touch screen events
     * that should be handled normally.
     * Default implementation provides motion event to activity child fragments.
     *
     * @param ev The touch screen event.
     * @return true if this event was consumed.
     */
    @Override
    public boolean dispatchTouchEvent(final MotionEvent ev) {
        final boolean superRes = super.dispatchTouchEvent(ev);

        boolean childRes = false;
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final List<Fragment> children = fragmentManager.getFragments();
        if (children != null) {
            for (final Fragment child : children) {
                if (child != null && child.getView() != null &&
                        DispatchTouchEventListener.class.isInstance(child)
                        && child.getUserVisibleHint()) {
                    childRes |= ((DispatchTouchEventListener) child).dispatchTouchEvent(ev);
                }
            }
        }

        return childRes || superRes;
    }

    @Override
    public List<String> getModuleSubscription() {
        return StringUtils.arrayToList(
                ActivityUnion.NAME,
                MailUnion.NAME
        );
    }

    @Override
    public void clearBackStack() {
        BackStack.clearBackStack(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int i = 0; i < permissions.length; i++) {
            PreferencesUtils.putInt(this, permissions[i], grantResults[i]);
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                onPermisionGranted(permissions[i]);
            } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                onPermisionDenied(permissions[i]);
            }
        }
    }

    @Override
    public int getState() {
        return mStateObservable.getState();
    }

    @Override
    public void setState(int state) {
    }

    @Override
    public void lockOrientation() {
        if (!validate()) return;

        switch (((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation()) {
            // Portrait
            case Surface.ROTATION_0:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;

            //Landscape
            case Surface.ROTATION_90:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;

            // Reversed portrait
            case Surface.ROTATION_180:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                break;

            // Reversed landscape
            case Surface.ROTATION_270:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                break;

            default:
                break;
        }
    }

    @Override
    public void lockOrientation(int orientation) {
        if (!validate()) return;

        setRequestedOrientation(orientation);
    }

    @Override
    public void unlockOrientation() {
        if (!validate()) return;

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    @Override
    public Result<Boolean> validateExt() {
        return new Result<>(getState() != ViewStateObserver.STATE_DESTROY && !isFinishing());
    }

    @Override
    public boolean validate() {
        return validateExt().getData();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setStatusBarColor(final int color) {
        if (!validate()) return;

        if (ApplicationUtils.hasLollipop()) {
            getWindow().setStatusBarColor(color);
        }
    }

    @Override
    public synchronized void exit() {
        if (ApplicationUtils.hasLollipop()) {
            super.finishAndRemoveTask();
        } else if (ApplicationUtils.hasJellyBean()) {
            super.finishAffinity();
        } else {
            super.finish();
        }
    }

    @Override
    public void onDialogResult(DialogResultEvent event) {
        if (!validate()) return;

        final Bundle bundle = event.getResult();
        if (bundle != null && bundle.getInt("id", -1) == R.id.dialog_request_permissions) {
            final String button = bundle.getString(MaterialDialogExt.BUTTON);
            if (button != null && button.equalsIgnoreCase(MaterialDialogExt.POSITIVE)) {
                final Intent intent = new Intent();
                final String packageName = getPackageName();
                intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }
    }

    @Override
    public synchronized void onPermisionGranted(final String permission) {
    }

    @Override
    public synchronized void onPermisionDenied(final String permission) {
    }

    @Override
    public View getRootView() {
        final View view = findView(R.id.root);
        if (view != null) {
            return view;
        }

        return ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
    }

    @Override
    public AbsActivity getActivity() {
        return this;
    }

    @Override
    public void showProgressDialog() {
        showProgressDialog(getString(R.string.loading));
    }

    @Override
    public void showProgressDialog(String message) {
        if (validate()) {
            if (mMaterialDialogWeakReference == null) {
                final MaterialDialog.Builder builder = new MaterialDialog.Builder(AbsActivity.this);
                if (StringUtils.isNullOrEmpty(message)) {
                    message = getString(R.string.loading);
                }
                builder.content(message);
                builder.progress(true, 0);
                builder.cancelable(false);
                final MaterialDialog dialog = builder.build();
                mMaterialDialogWeakReference = new WeakReference<>(dialog);
                dialog.show();
            } else {
                if (mMaterialDialogWeakReference.get() != null) {
                    mMaterialDialogWeakReference.get().setContent(message);
                }
            }
        }
    }

    @Override
    public void hideProgressDialog() {
        if (!validate()) return;

        if (mMaterialDialogWeakReference != null && mMaterialDialogWeakReference.get() != null) {
            mMaterialDialogWeakReference.get().dismiss();
            mMaterialDialogWeakReference = null;
        }
    }

    @Override
    public void setFullScreen() {
        if (!validate()) return;

        if (!ApplicationUtils.hasLollipop()) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    public void addStateObserver(final Stateable stateable) {
        mStateObservable.addObserver(stateable);
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
    public void hideKeyboard() {
        if (!validate()) return;

        final InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            View view = getCurrentFocus();
            if (view == null) {
                view = getRootView();
            }
            if (view != null) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void showKeyboard(View view) {
        if (!validate()) return;

        if (view != null) {
            new KeyboardRunnable(this, view).run();
        }
    }

    @Override
    public void showKeyboard(View view, int mode) {
        if (!validate()) return;

        if (view != null) {
            new KeyboardRunnable(this, view, mode).run();
        }
    }

    @Override
    public int getAccentColor() {
        final TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        return typedValue.data;
    }

    @Override
    public int getPrimaryColor() {
        final TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    /**
     * Called when the activity has detected the user's press of the back
     * key. The default implementation simply finishes the current activity,
     * but you can override this to do whatever you want.
     */
    public void onActivityBackPressed() {
        super.onBackPressed();
    }
}


