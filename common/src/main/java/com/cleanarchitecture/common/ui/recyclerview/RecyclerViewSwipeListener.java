package com.cleanarchitecture.common.ui.recyclerview;

import android.support.v7.widget.RecyclerView;

public interface RecyclerViewSwipeListener {

    void onSwiped(RecyclerView.ViewHolder viewHolder, int direction);

    boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target);

}
