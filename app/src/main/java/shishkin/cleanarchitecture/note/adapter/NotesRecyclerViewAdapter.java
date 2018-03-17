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

        private TextView title;
        private TextView text;
        private TextView created;
        private TextView modified;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);

            title = findView(R.id.title);
            text = findView(R.id.note);
            created = findView(R.id.created);
            modified = findView(R.id.modified);
        }

        void bind(@NonNull final Note note) {
            created.setText(StringUtils.formatDate(note.getCreated()));
            if (note.getModified() != null) {
                modified.setText(StringUtils.formatDate(note.getModified()));
            }

            final NoteJson noteJson = new Gson().fromJson(note.getNote(), NoteJson.class);
            if (noteJson != null) {
                title.setText(noteJson.getTitle());
                final List<NoteItem> items = noteJson.getItems();
                if (items != null && !items.isEmpty()) {
                    final StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < items.size(); i++) {
                        sb.append("" + (i+1) + ". " + items.get(i).getTitle());
                    }
                    text.setText(sb.toString());
                }
            }
        }
    }

}
