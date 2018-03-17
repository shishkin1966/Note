package com.cleanarchitecture.sl.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cleanarchitecture.common.net.Connectivity;
import com.cleanarchitecture.common.utils.StringUtils;
import com.cleanarchitecture.sl.R;
import com.cleanarchitecture.sl.event.ui.ShowMessageEvent;
import com.cleanarchitecture.sl.model.AbsModel;
import com.cleanarchitecture.sl.observe.impl.NetworkBroadcastReceiverObservable;
import com.cleanarchitecture.sl.sl.ApplicationModule;
import com.cleanarchitecture.sl.sl.ErrorModule;
import com.cleanarchitecture.sl.sl.ObservableSubscriber;
import com.cleanarchitecture.sl.sl.ObservableUnion;
import com.cleanarchitecture.sl.sl.SLUtil;


import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import static com.cleanarchitecture.common.ui.BaseSnackbar.MESSAGE_TYPE_WARNING;

/**
 * Created by Shishkin on 18.11.2017.
 */

public abstract class AbsToolbarFragment<M extends AbsModel> extends AbsContentFragment<M>
        implements ToolbarFragment, View.OnClickListener, ObservableSubscriber<Intent> {

    private static final String NAME = AbsToolbarFragment.class.getName();

    public static final Integer POPOP_MENU_ITEM_STATE_ENABLED = 0;
    public static final Integer POPOP_MENU_ITEM_STATE_DISABLED = 1;
    public static final Integer POPOP_MENU_ITEM_STATE_REMOVED = 2;

    private Toolbar mToolbar;
    private ImageView mMenu;
    private ImageView mItem;
    private int mMenuId = 0;
    private PopupMenu mPopupMenu;
    private boolean mPopupMenuShow = false;
    private Map<Integer, Integer> mStateMenuItems = Collections.synchronizedMap(new ConcurrentHashMap<Integer, Integer>());

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mToolbar = findView(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

        mMenu = findView(R.id.toolbar_menu);
        if (mMenu != null) {
            mMenu.setOnClickListener(this);
        }

        mItem = findView(R.id.toolbar_item);
        if (mItem != null) {
            mItem.setOnClickListener(this);
        }

        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        findView(R.id.back).setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Connectivity.isNetworkConnected(getContext())) {
            setToolbarColor(getPrimaryColor());
        } else {
            setToolbarColor(getAccentColor());
        }

        prepareToolbar();
    }

    @Override
    public abstract void prepareToolbar();

    @Override
    public void setTitle(String title) {
        if (validate()) {
            final TextView view = mToolbar.findViewById(R.id.toolbar_title);
            if (view != null) {
                view.setText(title);
            }
        }
    }

    @Override
    public void setToolbarColor(int color) {
        if (validate()) {
            mToolbar.setBackgroundColor(color);
        }
    }

    @Override
    public void onClick(View v) {
        if (validate()) {
            final int id = v.getId();
            if (id == R.id.back) {
                exit();
            } else if (id == R.id.toolbar_menu) {
                if (mPopupMenuShow) {
                    mPopupMenu.dismiss();
                } else {
                    showPopupMenu(mMenu);
                }
            }
        }
    }

    private void onConnect() {
        setToolbarColor(getPrimaryColor());
    }

    private void onDisconnect() {
        setToolbarColor(getAccentColor());
        SLUtil.getActivityUnion().showSnackbar(new ShowMessageEvent(getString(R.string.network_disconnected), MESSAGE_TYPE_WARNING));
    }

    @Override
    public void setMenu(final int menuId, final boolean isVisible) {
        if (validate() && mMenu != null) {
            mMenuId = menuId;
            mMenu.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    private void showPopupMenu(final View view) {
        if (!validate()) return;

        if (view == null && mMenuId == 0) {
            return;
        }

        if (!isAdded()) return;

        final FragmentActivity activity = getActivity();
        if (activity != null) {
            try {
                mPopupMenuShow = true;
                final Context wrapper = new ContextThemeWrapper(activity, R.style.PopupMenu);
                mPopupMenu = new PopupMenu(wrapper, view);
                final Field mFieldPopup = mPopupMenu.getClass().getDeclaredField("mPopup");
                mFieldPopup.setAccessible(true);
                final MenuPopupHelper mPopup = (MenuPopupHelper) mFieldPopup.get(mPopupMenu);
                mPopupMenu.inflate(mMenuId);
                for (int i = 0; i < mPopupMenu.getMenu().size(); i++) {
                    if (mPopupMenu.getMenu().getItem(i).getIcon() != null) {
                        mPopup.setForceShowIcon(true);
                        break;
                    }
                }
                mPopupMenu.setOnMenuItemClickListener(this::onMenuItemClick);
                mPopupMenu.setOnDismissListener(this::onDismiss);
                if (!mStateMenuItems.isEmpty()) {
                    final Menu menu = mPopupMenu.getMenu();
                    for (Map.Entry<Integer, Integer> entry : mStateMenuItems.entrySet()) {
                        final MenuItem item = menu.findItem(entry.getKey());
                        if (item != null) {
                            if (entry.getValue() == POPOP_MENU_ITEM_STATE_DISABLED) {
                                item.setEnabled(false);
                            } else if (entry.getValue() == POPOP_MENU_ITEM_STATE_REMOVED) {
                                menu.removeItem(entry.getKey());
                            }
                        }
                    }
                }
                mPopupMenu.show();
            } catch (Exception e) {
                ErrorModule.getInstance().onError(NAME, e);
            }
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return true;
    }

    private void onDismiss(final PopupMenu menu) {
        mPopupMenuShow = false;
        mPopupMenu = null;
    }

    private void dismissMenu() {
        if (mPopupMenu != null) {
            mPopupMenu.dismiss();
            mPopupMenu = null;
        }
    }

    @Override
    public void setMenuItemState(final int id, final int state) {
        mStateMenuItems.put(id, state);
    }

    @Override
    public void onDestroyView() {
        dismissMenu();

        super.onDestroyView();
    }

    @Override
    public void setItem(final int itemId, final boolean isVisible) {
        if (validate()) {
            if (itemId > 0) {
                mItem.setImageDrawable(VectorDrawableCompat.create(getResources(), itemId, null));
            }
            mItem.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public List<String> getObservable() {
        return StringUtils.arrayToList(NetworkBroadcastReceiverObservable.NAME);
    }

    @Override
    public void onChange(Intent intent) {
        if (validate()) {
            if (Connectivity.isNetworkConnected(ApplicationModule.getInstance())) {
                onConnect();
            } else {
                onDisconnect();
            }
        }
    }

    @Override
    public List<String> getModuleSubscription() {
        return StringUtils.arrayToList(super.getModuleSubscription(), ObservableUnion.NAME);
    }


}
