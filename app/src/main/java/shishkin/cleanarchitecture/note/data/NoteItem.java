package shishkin.cleanarchitecture.note.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class NoteItem {

    @SerializedName("title")
    private String mTtile;

    @SerializedName("checked")
    private boolean mItems;

}
