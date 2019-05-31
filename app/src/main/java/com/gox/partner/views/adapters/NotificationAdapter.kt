package com.gox.partner.views.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gox.base.utils.ViewUtils.getTimeDifference
import com.gox.partner.R
import com.gox.partner.databinding.NotificationListitemBinding
import com.gox.partner.interfaces.CustomClickListner
import com.gox.partner.models.NotificationResponse

class NotificationAdapter(val activity: FragmentActivity?, val notificationResponseData: NotificationResponse.ResponseData.Notification)
    : RecyclerView.Adapter<NotificationAdapter.MyViewHolder>(), CustomClickListner {

    private lateinit var context:Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val inflate = DataBindingUtil.inflate<NotificationListitemBinding>(LayoutInflater.from(parent.context)
                , R.layout.notification_listitem, parent, false)
        return MyViewHolder(inflate)
    }

    override fun getItemCount(): Int = notificationResponseData.data.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()
        holder.notificationListitemBinding.notificationTimeTv.text = getTimeDifference(notificationResponseData
                .data[position].created_at)
        holder.notificationListitemBinding.titlenotificationListTv.text = notificationResponseData.data[position].title
        holder.notificationListitemBinding.descriptionNotificationTv.text = notificationResponseData.data[position].descriptions
        Glide.with(activity!!).load(notificationResponseData.data[position].image)
                .into(holder.notificationListitemBinding.notificationImg)
    }


    inner class MyViewHolder(itemView: NotificationListitemBinding) : RecyclerView.ViewHolder(itemView.root) {

        val notificationListitemBinding = itemView

        fun bind() {
            notificationListitemBinding.descriptionNotificationTv.setShowingLine(3)
            notificationListitemBinding.descriptionNotificationTv.addShowMoreText("show more")
            notificationListitemBinding.descriptionNotificationTv.addShowLessText("show less")
            notificationListitemBinding.descriptionNotificationTv.setShowMoreColor(ContextCompat.getColor(context,R.color.colorBasePrimary))
            notificationListitemBinding.descriptionNotificationTv.setShowLessTextColor(ContextCompat.getColor(context,R.color.colorBasePrimary))

        }


    }

    override fun onListClickListner() {

        Log.d("currentadapter", "onListClickListener")
    }

}