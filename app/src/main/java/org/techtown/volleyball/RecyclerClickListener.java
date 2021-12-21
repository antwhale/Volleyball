package org.techtown.volleyball;

import android.view.View;
import org.techtown.volleyball.news3.RSSNewsAdapter.ViewHolder;

import org.techtown.volleyball.news3.RSSNewsAdapter;
import org.techtown.volleyball.recyclerviewadapter.InstaRecyclerAdapter;

public interface RecyclerClickListener {
     public void onItemClick(RSSNewsAdapter.ViewHolder holder, View view, int position);
     public void onItemClick(InstaRecyclerAdapter.RecyclerViewViewHolder holder, View view, int position);
}
