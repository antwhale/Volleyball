package org.techtown.volleyball.news3;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.techtown.volleyball.R;
import org.techtown.volleyball.RecyclerClickListener;
import org.techtown.volleyball.recyclerviewadapter.InstaRecyclerAdapter;

import java.util.ArrayList;

public class RSSNewsAdapter extends RecyclerView.Adapter<RSSNewsAdapter.ViewHolder>
        implements RecyclerClickListener {
    ArrayList<RSSNewsItem> items = new ArrayList<RSSNewsItem>();
    Context context;
    RecyclerClickListener listener;

    public RSSNewsAdapter(ArrayList<RSSNewsItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.node_item, viewGroup, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ViewHolder vh = (ViewHolder) viewHolder;

        //현재번째(position) 아이템 얻어오기
        RSSNewsItem item = items.get(position);
        String html = item.getTitle();
        vh.mText01.setText(Html.fromHtml(html));
        //vh.mText02.setText(item.getDescription());


        //이미지는 없을 수도 있음.
        if(item.getImgUrl()==null){
            vh.mIcon.setImageResource(R.drawable.media);
        }else{
            vh.mIcon.setVisibility(View.VISIBLE);
            //네트워크에 있는 이미지를 보여주려면
            //별도의 Thread가 필요한데 이를 편하게
            //해주는 Library사용(Glide library)

            Glide.with(context).load(item.getImgUrl()).into(vh.mIcon);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(RSSNewsItem item) {
        items.add(item);
    }

    public void setItems(ArrayList<RSSNewsItem> items) {
        this.items = items;
    }

    public RSSNewsItem getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, RSSNewsItem item) {
        items.set(position, item);
    }

    public void setOnItemClickListener(RecyclerClickListener listener) {
        this.listener = listener;
    }


    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    @Override
    public void onItemClick(InstaRecyclerAdapter.RecyclerViewViewHolder holder, View view, int position) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView mIcon;
            private TextView mText01;
            private TextView mText02;

            public ViewHolder(View itemView, final RecyclerClickListener listener) {
                super(itemView);

                mIcon = itemView.findViewById(R.id.iconItem);
                mText01 = itemView.findViewById(R.id.dataItem01);
                mText02 = itemView.findViewById(R.id.dataItem02);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAdapterPosition();
                        if (listener != null) {
                            listener.onItemClick(ViewHolder.this, view, position);
                        }
                    }
                });
            }

    }

}

