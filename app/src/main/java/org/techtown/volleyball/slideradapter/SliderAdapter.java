package org.techtown.volleyball.slideradapter;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import org.techtown.volleyball.MainActivity;
import org.techtown.volleyball.MainFragment;
import org.techtown.volleyball.NewsActivity;
import org.techtown.volleyball.R;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH>{
    private Context context;
    private List<SliderItem> mSliderItems = new ArrayList<>();

    public SliderAdapter(Context context) {
        this.context = context;
    }

    public void renewItems(List<SliderItem> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        //notifyDataSetChanged();
    }

    public void addItem(SliderItem sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        SliderItem sliderItem = mSliderItems.get(position);

        //이미지는 없을 수도 있음.
        if(sliderItem.getThumbUrl()==null){
            viewHolder.imageView.setImageResource(R.drawable.media);
        }else{
            //viewHolder.imageView.setVisibility(View.VISIBLE);
            //네트워크에 있는 이미지를 보여주려면
            //별도의 Thread가 필요한데 이를 편하게
            //해주는 Library사용(Glide library)

            Glide.with(viewHolder.imageView.getContext()).load(sliderItem.getThumbUrl()).into(viewHolder.imageView);
        }



        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //여기서 액티비티 띄우 네이버 영상
                if(position != RecyclerView.NO_POSITION){
                    //링크가져오기
                    String link = "https://tv.naver.com" + mSliderItems.get(position).naverUrl;
                    //인텐트만들기
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    context.startActivity(intent);
                    /*showNaverPage("https://tv.naver.com" + mSliderItems.get(position).naverUrl);
                    Log.d("jackson" ,"https://tv.naver.com" + mSliderItems.get(position).naverUrl );*/
                }
            }
        });
    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    //Slider Adapter 클래스
    public class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        ImageView imageView;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);

        }
    }

    public void showNaverPage(String link){
        Intent intent = new Intent(context, NewsActivity.class);
        intent.putExtra("Link", link);
        context.startActivity(intent);
    }

}
