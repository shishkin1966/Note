package shishkin.cleanarchitecture.note.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;


import java.util.List;


import shishkin.cleanarchitecture.note.data.Note;

import static android.arch.persistence.room.OnConflictStrategy.ROLLBACK;

@Dao
public interface NoteDao {

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("DELETE FROM " + Note.TABLE)
    void delete();

    @Query("SELECT * FROM " + Note.TABLE + " ORDER BY " + Note.Columns.created + " ASC")
    List<Note> get();

}
