package shishkin.cleanarchitecture.note.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.cleanarchitecture.sl.data.AbsEntity;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shishkin on 10.01.2018.
 */

@Entity(tableName = Note.TABLE)
public class Note extends AbsEntity {

    public static final String TABLE = "Note";

    public interface Columns {
        String id = "id";
        String created = "created";
        String modified = "modified";
        String note = "note";
    }

    public static final String[] PROJECTION = {
            Columns.id,
            Columns.created,
            Columns.modified,
            Columns.note
    };

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @NonNull
    private Long mId;

    @ColumnInfo(name = Columns.created)
    @SerializedName(Columns.created)
    @NonNull
    private Long mCreated;

    @ColumnInfo(name = Columns.modified)
    @SerializedName(Columns.modified)
    private Long mModified;

    @ColumnInfo(name = Columns.note)
    @SerializedName(Columns.note)
    private String mNote;

    public Note() {
        mCreated = System.currentTimeMillis();
    }

    @NonNull
    public Long getId() {
        return mId;
    }

    public void setId(@NonNull Long id) {
        this.mId = id;
    }

    @NonNull
    public Long getCreated() {
        return mCreated;
    }

    public void setCreated(@NonNull Long created) {
        this.mCreated = created;
    }

    public Long getModified() {
        return mModified;
    }

    public void setModified(Long modified) {
        this.mModified = modified;
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String note) {
        this.mNote = note;
    }
}
