package org.techtown.volleyball.recyclerviewadapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.techtown.volleyball.MainActivity;
import org.techtown.volleyball.NewsActivity;
import org.techtown.volleyball.R;
import org.techtown.volleyball.RecyclerClickListener;

import java.util.List;

public class InstaRecyclerAdapter extends RecyclerView.Adapter<InstaRecyclerAdapter.RecyclerViewViewHolder>{
    Context context;
    List<InstaRecyclerItem> RecyclerList;
    RecyclerClickListener listener;

    public InstaRecyclerAdapter(Context context, List<InstaRecyclerItem> recyclerList) {
        this.context = context;
        this.RecyclerList = recyclerList;
    }

    public InstaRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void renewItems(List<InstaRecyclerItem> RecyclerItems) {
        this.RecyclerList = RecyclerItems;
        notifyDataSetChanged();
    }

    public void deleteItems() {
        this.RecyclerList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_layout_item, parent, false);

        return new RecyclerViewViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewViewHolder holder, int position) {
        Glide.with(context).load(RecyclerList.get(position).getImg_url()).into(holder.thumbnail);
        //notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return RecyclerList.size();
    }

    public class RecyclerViewViewHolder extends RecyclerView.ViewHolder{
        //private TextView publishedAt, title, description;
        private ImageView thumbnail;

        public RecyclerViewViewHolder(View itemView, final RecyclerClickListener listener){
            super(itemView);

            //publishedAt = itemView.findViewById(R.id.publishedAt);
            //title = itemView.findViewById(R.id.title);
            //description = itemView.findViewById(R.id.description);
            thumbnail = itemView.findViewById(R.id.thumbnail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getBindingAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        showInstaPage(RecyclerList.get(pos).insta_url);
                    }
                }
            });
        }
    }

    public void showInstaPage(String link){
        Intent intent = new Intent(context, NewsActivity.class);
        intent.putExtra("Link", link);
        context.startActivity(intent);
    }
}
