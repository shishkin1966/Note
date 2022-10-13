package shishkin.cleanarchitecture.note.db;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import androidx.annotation.NonNull;


import shishkin.cleanarchitecture.note.dao.NoteDao;
import shishkin.cleanarchitecture.note.data.Note;

@Database(entities = {Note.class}, version = NotesDb.VERSION, exportSchema = false)
public abstract class NotesDb extends RoomDatabase {

    public static final String NAME = "notes.db";
    public static final int VERSION = 1;

    public abstract NoteDao NoteDao();

    private static volatile NotesDb sInstance;

    public static NotesDb getInstance(Context context) {
        if (sInstance == null) {
            sInstance = buildDatabase(context);
        }
        return sInstance;
    }

    private static NotesDb buildDatabase(Context context) {
        if (context == null) return null;

        return Room.databaseBuilder(context,
                NotesDb.class,
                NAME)
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);

                        onCreateDatabase(db);
                    }

                    @Override
                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                        super.onOpen(db);

                        onOpenDatabase(db);
                    }
                })
                .build();
    }

    private static void onCreateDatabase(final SupportSQLiteDatabase db) {
    }

    private static void onOpenDatabase(final SupportSQLiteDatabase db) {
    }


}