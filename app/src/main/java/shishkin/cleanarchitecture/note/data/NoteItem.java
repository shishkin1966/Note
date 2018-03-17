package shishkin.cleanarchitecture.note.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class NoteItem {

    @SerializedName("title")
    private String mTitle;

    @SerializedName("checked")
    private boolean mChecked;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        this.mChecked = checked;
    }

}
