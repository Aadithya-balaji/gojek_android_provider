package com.gox.partner.views.on_board

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.viewpager.widget.PagerAdapter
import com.gox.partner.R
import java.util.*

class IntroSliderAdapter(private val mContext: Context, private val onBoardItems: ArrayList<OnBoardItem>)
    : PagerAdapter() {

    override fun getCount() = onBoardItems.size

    override fun isViewFromObject(@NonNull view: View, @NonNull `object`: Any): Boolean {
        return view === `object`
    }

    @NonNull
    override fun instantiateItem(@NonNull container: ViewGroup, position: Int): Any {
        val itemView = LayoutInflater.from(mContext).inflate(R.layout.pager_item, container, false)

        val item = onBoardItems[position]

        val imageView = itemView.findViewById<ImageView>(R.id.img_pager_item)
        imageView.setImageResource(item.imageID)

        val tvTitle = itemView.findViewById<TextView>(R.id.title)
        tvTitle.text = item.title

        val tvContent = itemView.findViewById<TextView>(R.id.description)
        tvContent.text = item.description

        container.addView(itemView)

        return itemView
    }

    override fun destroyItem(@NonNull container: ViewGroup, position: Int, @NonNull `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }

}
