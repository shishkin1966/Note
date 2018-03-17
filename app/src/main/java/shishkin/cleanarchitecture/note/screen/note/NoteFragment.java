package shishkin.cleanarchitecture.note.screen.note;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.cleanarchitecture.sl.presenter.impl.OnBackPressedPresenter;
import com.cleanarchitecture.sl.ui.fragment.AbsToolbarFragment;
import com.google.gson.Gson;


import shishkin.cleanarchitecture.note.R;
import shishkin.cleanarchitecture.note.data.Note;
import shishkin.cleanarchitecture.note.data.NoteJson;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class NoteFragment extends AbsToolbarFragment<NoteModel> implements View.OnFocusChangeListener {

    public static final String OPERATION_INSERT = "OPERATION_INSERT";
    public static final String OPERATION_EDIT = "OPERATION_EDIT";

    private OnBackPressedPresenter mOnBackPressedPresenter = new OnBackPressedPresenter();
    private RecyclerView mRecyclerView;
    private NoteJson mNoteJson;
    private Note mNote;
    private String mOperation = OPERATION_EDIT;
    private EditText mCurrent;

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
        setHint(getString(R.string.note_title));
        setEdit(mNoteJson.getTitle(), true);
        getEdit().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        getEdit().setOnFocusChangeListener(this);
    }

    @Override
    public boolean onBackPressed() {
        mNoteJson.setTitle(getEdit().getText().toString());
        mNote.setNote(new Gson().toJson(mNoteJson));
        getModel().getPresenter().onBackPressed(mNote, mOperation);
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            mCurrent = (EditText) v;
        }
    }
}
