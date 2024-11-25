package org.techtown.volleyball.adapters

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.techtown.volleyball.R
import org.techtown.volleyball.news3.RSSNewsItem

class RecentNewsAdapter(val recentNewsItems: List<RSSNewsItem>, val context: Context) : RecyclerView.Adapter<RecentNewsAdapter.ViewHolder>() {

    lateinit var recentNewsClickListener: RecentNewsClickListener
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecentNewsAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.node_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecentNewsAdapter.ViewHolder, position: Int) {
        //현재번째(position) 아이템 얻어오기
        val item = recentNewsItems[position]
        val html = item.title
        holder.mText01.text = Html.fromHtml(html)

        //이미지는 없을 수도 있음
        if(item.imgUrl.isNullOrEmpty()) {
            holder.mIcon.setImageResource(R.drawable.media)
        } else {
            holder.mIcon.visibility = View.VISIBLE

            //네트워크에 있는 이미지를 보여주려면
            //별도의 Thread가 필요한데 이를 편하게
            //해주는 Library사용(Glide library)

            Glide.with(context).load(item.imgUrl).into(holder.mIcon)
        }
    }

    override fun getItemCount(): Int {
        return recentNewsItems.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mIcon : ImageView
        var mText01 : TextView
        var mText02 : TextView

        init {
            mIcon = itemView.findViewById(R.id.iconItem)
            mText01 = itemView.findViewById(R.id.dataItem01)
            mText02 = itemView.findViewById(R.id.dataItem02)

            itemView.setOnClickListener {
                recentNewsClickListener.onItemClick(this, it, absoluteAdapterPosition)
            }
        }
    }
}

interface RecentNewsClickListener {
    fun onItemClick(holder : RecentNewsAdapter.ViewHolder, view: View, position : Int)
}