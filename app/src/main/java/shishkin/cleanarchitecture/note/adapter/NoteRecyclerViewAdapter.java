package shishkin.cleanarchitecture.note.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.cleanarchitecture.common.ui.EditTextDebounced;
import com.cleanarchitecture.common.ui.EditTextListener;
import com.cleanarchitecture.common.ui.recyclerview.AbstractRecyclerViewAdapter;
import com.cleanarchitecture.common.ui.recyclerview.AbstractViewHolder;


import shishkin.cleanarchitecture.note.R;
import shishkin.cleanarchitecture.note.data.NoteItem;

public class NoteRecyclerViewAdapter extends AbstractRecyclerViewAdapter<NoteItem, NoteRecyclerViewAdapter.ViewHolder> {

    private RecyclerView mRecyclerView;

    public NoteRecyclerViewAdapter(@NonNull Context context) {
        super(context);

        setHasStableIds(false);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mRecyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.item_noteitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, NoteItem item, int position) {
        holder.bind(position, getItem(position));
    }

    public void setFocusItem(int position) {
        if (mRecyclerView != null) {
            final ViewHolder holder = (ViewHolder) mRecyclerView.findViewHolderForAdapterPosition(position);
            if (holder != null) {
                holder.setFocus();
            }
        }
    }

    static class ViewHolder extends AbstractViewHolder implements EditTextListener {

        private EditText titleView;
        private TextView numbertView;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);

            titleView = findView(R.id.title);
            numbertView = findView(R.id.number);
        }

        void bind(int position, @NonNull final NoteItem noteItem) {
            numbertView.setText("" + (position + 1) + ".");
            titleView.setText(noteItem.getTitle());
            titleView.setTag(noteItem);
            new EditTextDebounced(titleView, this);
        }

        @Override
        public void afterTextChanged(EditText editText) {
            final NoteItem item = (NoteItem) editText.getTag();
            if (item != null) {
                item.setTitle(editText.getText().toString());
            }
        }

        void setFocus() {
            titleView.requestFocus();
            titleView.requestFocusFromTouch();
        }
    }

}
