package com.cleanarchitecture.common.ui.recyclerview;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

public class SwipeMoveTouchHelper extends ItemTouchHelper.SimpleCallback {
    private AbstractRecyclerViewAdapter mMovieAdapter;
    private RecyclerViewSwipeListener mListener;

    public SwipeMoveTouchHelper(AbstractRecyclerViewAdapter movieAdapter, RecyclerViewSwipeListener listener) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);

        mMovieAdapter = movieAdapter;
        mListener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (mListener != null) {
            return mListener.onMove(recyclerView, viewHolder, target);
        } else {
            if (mMovieAdapter != null) {
                mMovieAdapter.move(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            }
        }
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (mListener != null) {
            mListener.onSwiped(viewHolder, direction);
        } else {
            if (mMovieAdapter != null) {
                mMovieAdapter.remove(viewHolder.getAdapterPosition());
            }
        }
    }

    public AbstractRecyclerViewAdapter getAdapter() {
        return mMovieAdapter;
    }

}