package shishkin.cleanarchitecture.note.screen.notes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.cleanarchitecture.common.ui.recyclerview.RecyclerViewSwipeListener;
import com.cleanarchitecture.common.ui.recyclerview.SwipeTouchHelper;
import com.cleanarchitecture.sl.presenter.impl.OnBackPressedPresenter;
import com.cleanarchitecture.sl.ui.fragment.AbsToolbarFragment;


import shishkin.cleanarchitecture.note.R;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class NotesFragment extends AbsToolbarFragment<NotesModel> implements RecyclerViewSwipeListener {

    private OnBackPressedPresenter mOnBackPressedPresenter = new OnBackPressedPresenter();
    private RecyclerView mRecyclerView;

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

        mRecyclerView = findView(R.id.list);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        /*
        mAdapter = new PedometerRecyclerViewAdapter(getContext());
        mAdapter.setOnItemClickListener((v, position, item) -> {
            if (position != mAdapter.getItemCount() - 1) {
                final PedometerPresenter pedometerPresenter = SafeUtils.cast(SLUtil.getPresenterUnion().getSubscriber(PedometerPresenter.NAME));
                if (pedometerPresenter != null) {
                    pedometerPresenter.setPage(0);
                }
                final PedometerMapPresenter pedometerMapPresenter = SafeUtils.cast(SLUtil.getPresenterUnion().getSubscriber(PedometerMapPresenter.NAME));
                if (pedometerMapPresenter != null) {
                    pedometerMapPresenter.setDay(item.getDay());
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        */

        final ItemTouchHelper.Callback callback = new SwipeTouchHelper(null, this);
        final ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);

    }

    @Override
    public void onDestroyView() {
        mRecyclerView.setAdapter(null);

        super.onDestroyView();
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

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //final Summary item = mAdapter.getItem(viewHolder.getAdapterPosition());
        //mDeleteSummary = viewHolder.getAdapterPosition();
        //SLUtil.getActivityUnion().showDialog(new ShowDialogEvent(R.id.dialog_delete_day, this.getName(), null, "Удалить данные за " + item.getDay() + "?", R.string.yes, R.string.no));
    }
}
