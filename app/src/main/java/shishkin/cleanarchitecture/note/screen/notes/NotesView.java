package shishkin.cleanarchitecture.note.screen.notes;

import com.cleanarchitecture.sl.ui.fragment.IFragment;


import java.util.List;


import shishkin.cleanarchitecture.note.data.Note;

/**
 * Created by Shishkin on 17.03.2018.
 */

public interface NotesView extends IFragment {

    void setItems(List<Note> items);

}
