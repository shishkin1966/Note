package shishkin.cleanarchitecture.note.screen.notes;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.cleanarchitecture.common.ui.recyclerview.RecyclerViewSwipeListener;
import com.cleanarchitecture.common.ui.recyclerview.SwipeMoveTouchHelper;
import com.cleanarchitecture.sl.presenter.impl.OnBackPressedPresenter;
import com.cleanarchitecture.sl.ui.fragment.AbsToolbarFragment;
import com.github.clans.fab.FloatingActionButton;


import java.util.List;


import shishkin.cleanarchitecture.note.R;
import shishkin.cleanarchitecture.note.adapter.NotesRecyclerViewAdapter;
import shishkin.cleanarchitecture.note.data.Note;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class NotesFragment extends AbsToolbarFragment<NotesModel> implements RecyclerViewSwipeListener, NotesView {

    private OnBackPressedPresenter mOnBackPressedPresenter = new OnBackPressedPresenter();
    private RecyclerView mRecyclerView;
    private FloatingActionButton mButton;
    private NotesRecyclerViewAdapter mAdapter;

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

        mButton = findView(R.id.add);
        mButton.setOnClickListener(getModel().getPresenter());

        addStateObserver(mOnBackPressedPresenter);

        mRecyclerView = findView(R.id.list);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new NotesRecyclerViewAdapter(getContext());
        mAdapter.setOnItemClickListener((v, position, item) -> {
            getModel().getRouter().showNote(item);
        });
        mRecyclerView.setAdapter(mAdapter);

        final ItemTouchHelper.Callback callback = new SwipeMoveTouchHelper(mAdapter, this);
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
        final int position = viewHolder.getAdapterPosition();
        getModel().getInteractor().removeNote(mAdapter.getItem(position));
        mAdapter.remove(position);
        mRecyclerView.post(() -> getModel().getPresenter().onNotesChanged());
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mAdapter.move(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        mRecyclerView.post(() -> getModel().getPresenter().onNotesChanged());
        return true;
    }

    @Override
    public void setItems(List<Note> items) {
        if (items != null) {
            mAdapter.setItems(items);
        }
    }

    @Override
    public List<Note> getItems() {
        return mAdapter.getItems();
    }
}
