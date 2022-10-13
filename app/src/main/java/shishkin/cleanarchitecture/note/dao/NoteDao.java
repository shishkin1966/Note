package shishkin.cleanarchitecture.note.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;


import java.util.List;


import shishkin.cleanarchitecture.note.data.Note;

import static androidx.room.OnConflictStrategy.ROLLBACK;

@Dao
public interface NoteDao {

    @Transaction
    @Insert(onConflict = ROLLBACK)
    void insert(Note note);

    @Transaction
    @Update(onConflict = ROLLBACK)
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("DELETE FROM " + Note.TABLE)
    void delete();

    @Query("SELECT * FROM " + Note.TABLE + " ORDER BY " + Note.Columns.poradok + " ASC")
    List<Note> get();

}
