package shishkin.cleanarchitecture.note.screen.note;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.cleanarchitecture.common.ui.recyclerview.RecyclerViewSwipeListener;
import com.cleanarchitecture.common.ui.recyclerview.SwipeTouchHelper;
import com.cleanarchitecture.common.utils.StringUtils;
import com.cleanarchitecture.sl.presenter.impl.OnBackPressedPresenter;
import com.cleanarchitecture.sl.ui.fragment.AbsToolbarFragment;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.List;


import shishkin.cleanarchitecture.note.R;
import shishkin.cleanarchitecture.note.adapter.NoteRecyclerViewAdapter;
import shishkin.cleanarchitecture.note.data.Note;
import shishkin.cleanarchitecture.note.data.NoteItem;
import shishkin.cleanarchitecture.note.data.NoteJson;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class NoteFragment extends AbsToolbarFragment<NoteModel> implements View.OnFocusChangeListener, RecyclerViewSwipeListener {

    public static final String OPERATION_INSERT = "OPERATION_INSERT";
    public static final String OPERATION_EDIT = "OPERATION_EDIT";

    private OnBackPressedPresenter mOnBackPressedPresenter = new OnBackPressedPresenter();
    private RecyclerView mRecyclerView;
    private NoteJson mNoteJson;
    private Note mNote;
    private String mOperation = OPERATION_EDIT;
    private EditText mCurrent;
    private NoteRecyclerViewAdapter mAdapter;
    private View mAddButton;
    private EditText mTitle;

    public static NoteFragment newInstance(final Note note) {
        final NoteFragment f = new NoteFragment();
        final Bundle bundle = new Bundle();
        bundle.putParcelable("note", note);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setModel(new NoteModel(this));

        mNote = getArguments().getParcelable("note");
        mNoteJson = new Gson().fromJson(mNote.getNote(), NoteJson.class);
        if (mNoteJson == null) {
            mOperation = OPERATION_INSERT;
            mNoteJson = new NoteJson();
        }

        addStateObserver(mOnBackPressedPresenter);

        mRecyclerView = findView(R.id.list);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new NoteRecyclerViewAdapter(getContext());
        mAdapter.setItems(mNoteJson.getItems());
        mRecyclerView.setAdapter(mAdapter);

        final ItemTouchHelper.Callback callback = new SwipeTouchHelper(null, this);
        final ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);

        mTitle = findView(R.id.title);
        mTitle.setText(mNoteJson.getTitle());

        mAddButton = findView(R.id.add);
        mAddButton.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        mRecyclerView.setAdapter(null);

        super.onDestroyView();
    }


    @Override
    public String getName() {
        return NoteFragment.class.getName();
    }

    @Override
    public void prepareToolbar() {
        if (mOperation.equals(NoteFragment.OPERATION_INSERT)) {
            setTitle(getString(R.string.note_new));
        } else {
            setTitle(getString(R.string.note_edit));
        }
    }

    @Override
    public boolean onBackPressed() {
        mNoteJson.setTitle(StringUtils.allTrim(mTitle.getText().toString()));
        final List<NoteItem> list = new ArrayList<>();
        for (NoteItem item : mAdapter.getItems()) {
            if (!StringUtils.isNullOrEmpty(StringUtils.allTrim(item.getTitle()))) {
                list.add(item);
            }
        }
        mNoteJson.setItems(list);
        getModel().getPresenter().onBackPressed(mNote, mNoteJson, mOperation);
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            mCurrent = (EditText) v;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                mAdapter.add(new NoteItem());
                mRecyclerView.post(() -> mAdapter.setFocusItem(mAdapter.getItemCount() - 1));
                break;

            default:
                super.onClick(v);
                break;
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        mAdapter.remove(position);
    }
}
