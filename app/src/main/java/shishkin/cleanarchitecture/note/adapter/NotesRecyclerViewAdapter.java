package shishkin.cleanarchitecture.note.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cleanarchitecture.common.ui.recyclerview.AbstractRecyclerViewAdapter;
import com.cleanarchitecture.common.ui.recyclerview.AbstractViewHolder;
import com.cleanarchitecture.common.utils.StringUtils;
import com.google.gson.Gson;


import java.util.List;


import shishkin.cleanarchitecture.note.R;
import shishkin.cleanarchitecture.note.data.Note;
import shishkin.cleanarchitecture.note.data.NoteItem;
import shishkin.cleanarchitecture.note.data.NoteJson;

public class NotesRecyclerViewAdapter extends AbstractRecyclerViewAdapter<Note, NotesRecyclerViewAdapter.ViewHolder> {

    public NotesRecyclerViewAdapter(@NonNull Context context) {
        super(context);

        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return getItems().get(position).getId();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.item_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, Note item, int position) {
        holder.bind(getItem(position));
    }

    static class ViewHolder extends AbstractViewHolder {

        private TextView titleView;
        private TextView textView;
        private TextView createdView;
        private TextView modifiedView;
        private View createdLL;
        private View modifiedLL;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);

            titleView = findView(R.id.title);
            textView = findView(R.id.note);
            createdView = findView(R.id.created);
            modifiedView = findView(R.id.modified);
            createdLL = findView(R.id.ll_created);
            modifiedLL = findView(R.id.ll_modified);
        }

        void bind(@NonNull final Note note) {
            createdView.setText(StringUtils.formatDate(note.getCreated()));
            if (note.getModified() != null) {
                modifiedView.setText(StringUtils.formatDate(note.getModified()));
            }
            if (note.getModified() == null) {
                createdLL.setVisibility(View.VISIBLE);
                modifiedLL.setVisibility(View.GONE);
            } else {
                createdLL.setVisibility(View.GONE);
                modifiedLL.setVisibility(View.VISIBLE);
            }

            final NoteJson noteJson = new Gson().fromJson(note.getNote(), NoteJson.class);
            if (noteJson != null) {
                final String title = noteJson.getTitle();
                titleView.setText(title);
                if (StringUtils.isNullOrEmpty(title)) {
                    titleView.setVisibility(View.GONE);
                } else {
                    titleView.setVisibility(View.VISIBLE);
                }

                final List<NoteItem> items = noteJson.getItems();
                if (items != null && !items.isEmpty()) {
                    final StringBuilder sb = new StringBuilder();
                    int row = 1;
                    for (int i = 0; i < items.size(); i++) {
                        if (!StringUtils.isNullOrEmpty(StringUtils.allTrim(items.get(i).getTitle()))) {
                            if (row != 1) {
                                sb.append("\n");
                            }
                            sb.append("" + (row) + ". " + items.get(i).getTitle());
                            row++;
                        }
                    }
                    textView.setText(sb.toString());
                    textView.setVisibility(View.VISIBLE);
                } else {
                    textView.setVisibility(View.GONE);
                }
            }
        }
    }

}
