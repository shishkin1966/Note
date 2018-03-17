package shishkin.cleanarchitecture.note.screen.notes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.cleanarchitecture.sl.presenter.impl.OnBackPressedPresenter;
import com.cleanarchitecture.sl.sl.ApplicationModule;
import com.cleanarchitecture.sl.ui.fragment.AbsToolbarFragment;


import shishkin.cleanarchitecture.note.R;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class NotesFragment extends AbsToolbarFragment<NotesModel> {

    private OnBackPressedPresenter mOnBackPressedPresenter = new OnBackPressedPresenter();

    public static NotesFragment newInstance() {
        return new NotesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setModel(new NotesModel(this));

        addStateObserver(mOnBackPressedPresenter);
    }

    @Override
    public String getName() {
        return NotesFragment.class.getName();
    }

    @Override
    public void prepareToolbar() {
        setTitle(getString(R.string.app_name));
        setMenu(R.menu.main_menu, true);
    }

    @Override
    public boolean onBackPressed() {
        mOnBackPressedPresenter.onClick();
        return true;
    }

    @Override
    public boolean isTop() {
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting:
                getModel().getRouter().showSetting();
                break;

            case R.id.menu_backup_dp:
                getModel().getRouter().backupDb();
                break;

            case R.id.menu_restore_dp:
                getModel().getRouter().restoreDb();
                break;

            case R.id.menu_exit:
                getModel().getRouter().finishApplication();
                break;
        }
        return true;
    }

}
