package com.cleanarchitecture.sl.ui.fragment;

import android.content.Context;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cleanarchitecture.sl.R;
import com.cleanarchitecture.sl.model.AbsModel;
import com.cleanarchitecture.sl.sl.ErrorModule;


import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Shishkin on 18.11.2017.
 */

public abstract class AbsToolbarFragment<M extends AbsModel> extends AbsContentFragment<M>
        implements ToolbarFragment, View.OnClickListener {

    private static final String NAME = AbsToolbarFragment.class.getName();

    public static final Integer POPOP_MENU_ITEM_STATE_ENABLED = 0;
    public static final Integer POPOP_MENU_ITEM_STATE_DISABLED = 1;
    public static final Integer POPOP_MENU_ITEM_STATE_REMOVED = 2;

    private Toolbar mToolbar;
    private ImageView mMenu;
    private ImageView mItem;
    private EditText mEdit;
    private TextView mTitle;
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

        mEdit = findView(R.id.toolbar_edit);

        mTitle = findView(R.id.toolbar_title);

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

        prepareToolbar();
    }

    @Override
    public abstract void prepareToolbar();

    @Override
    public void setTitle(String title) {
        if (validate()) {
            if (mTitle != null) {
                mTitle.setText(title);
                mTitle.setVisibility(View.VISIBLE);
            }

            if (mEdit != null) {
                mEdit.setVisibility(View.INVISIBLE);
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
    public void setEdit(final String text, final boolean isVisible) {
        if (validate()) {
            if (mEdit != null) {
                mEdit.setText(text);
                mEdit.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
            }

            if (!isVisible && mTitle != null) {
                mTitle.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void setHint(final String hint) {
        if (validate()) {
            if (mEdit != null) {
                mEdit.setHint(hint);
            }
        }
    }

    @Override
    public EditText getEdit() {
        return mEdit;
    }

}
