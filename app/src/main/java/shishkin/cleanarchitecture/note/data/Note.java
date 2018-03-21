package shishkin.cleanarchitecture.note.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.cleanarchitecture.sl.data.AbsEntity;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shishkin on 10.01.2018.
 */

@Entity(tableName = Note.TABLE)
public class Note extends AbsEntity implements Parcelable {

    public static final String TABLE = "Note";

    public interface Columns {
        String id = "id";
        String created = "created";
        String modified = "modified";
        String note = "note";
        String poradok = "poradok";
    }

    public static final String[] PROJECTION = {
            Columns.id,
            Columns.created,
            Columns.modified,
            Columns.note,
            Columns.poradok
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

    @ColumnInfo(name = Columns.poradok)
    @SerializedName(Columns.poradok)
    private int mPoradok;

    public Note() {
        mCreated = System.currentTimeMillis();
        mPoradok = 0;
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

    public int getPoradok() {
        return mPoradok;
    }

    public void setPoradok(int poradok) {
        this.mPoradok = poradok;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mId);
        dest.writeValue(this.mCreated);
        dest.writeValue(this.mModified);
        dest.writeString(this.mNote);
        dest.writeInt(this.mPoradok);
    }

    protected Note(Parcel in) {
        this.mId = (Long) in.readValue(Long.class.getClassLoader());
        this.mCreated = (Long) in.readValue(Long.class.getClassLoader());
        this.mModified = (Long) in.readValue(Long.class.getClassLoader());
        this.mNote = in.readString();
        this.mPoradok = in.readInt();
    }

    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel source) {
            return new Note(source);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}
