package shishkin.cleanarchitecture.note.screen.note;

import com.cleanarchitecture.sl.model.ModelInteractor;


import shishkin.cleanarchitecture.note.data.Note;
import shishkin.cleanarchitecture.note.sl.Repository;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class NoteInteractor implements ModelInteractor {

    public void insert(Note note) {
        Repository.insertNote(note);
    }

    public void update(Note note) {
        Repository.updateNote(note);
    }

}
