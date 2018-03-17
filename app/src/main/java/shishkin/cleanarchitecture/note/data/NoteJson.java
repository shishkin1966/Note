package shishkin.cleanarchitecture.note.data;

import com.google.gson.annotations.SerializedName;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class NoteJson {

    @SerializedName("title")
    private String mTitle;

    @SerializedName("items")
    private List<NoteItem> mItems = new ArrayList<>();

    public List<NoteItem> getItems() {
        return mItems;
    }

    public void setItems(List<NoteItem> items) {
        this.mItems = items;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }


}
