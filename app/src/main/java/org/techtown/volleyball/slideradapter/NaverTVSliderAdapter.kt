package org.techtown.volleyball.slideradapter

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.antwhale.AntwhaleImageSliderAdapter
import org.techtown.volleyball.R
import org.techtown.volleyball.data.entity.NaverTVItem

class NaverTVSliderAdapter(
    override var sliderImages: List<org.techtown.volleyball.data.entity.NaverTVItem>
) : AntwhaleImageSliderAdapter<org.techtown.volleyball.data.entity.NaverTVItem, NaverTVSliderAdapter.NaverTVViewHolder>() {
    val TAG = "NaverTVSliderAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NaverTVViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_slider_layout_item, parent, false)
        return NaverTVViewHolder(view)
    }

    override fun onBindViewHolder(holder: NaverTVViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(sliderImages[getRealPosition(position)].thumbUrl)
            .into(holder.imageView)

        holder.itemView.setOnClickListener { view ->
            Log.d(TAG, "Click sliderItem, position: " + position)
            Log.d(TAG, "sliderImages size: " + sliderImages.size)
            if(position != RecyclerView.NO_POSITION) {
                val link = sliderImages.get(getRealPosition(position)).naverUrl
                Log.d(TAG, "NaverTV link: " + link)
                Intent(Intent.ACTION_VIEW, Uri.parse(link)).also { intent ->
                    view.context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemListSize(): Int = sliderImages.size

    fun renewItems(naverTVItems : List<NaverTVItem>) {
        Log.d(TAG, "renewItems, size: " + naverTVItems.size)
        sliderImages = naverTVItems
        notifyDataSetChanged()
    }
    inner class NaverTVViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.imageView)
    }
}