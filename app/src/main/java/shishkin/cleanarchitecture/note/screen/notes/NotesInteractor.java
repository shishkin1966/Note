package shishkin.cleanarchitecture.note.screen.notes;

import com.cleanarchitecture.sl.model.ModelInteractor;


import shishkin.cleanarchitecture.note.data.Note;
import shishkin.cleanarchitecture.note.sl.Repository;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class NotesInteractor implements ModelInteractor {

    public void getNotes(String responseListener) {
        Repository.getNotes(responseListener);
    }

    public void removeNote(Note note) {
        Repository.removeNote(note);
    }
}
