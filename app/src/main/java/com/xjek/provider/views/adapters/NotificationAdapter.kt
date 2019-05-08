package com.xjek.provider.views.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xjek.base.utils.ViewUtils.getTimeDifference
import com.xjek.provider.R
import com.xjek.provider.interfaces.CustomClickListner
import com.xjek.provider.models.NotificationResponseData

class NotificationAdapter(val activity: FragmentActivity?, val notificationResponseData: NotificationResponseData)
    : RecyclerView.Adapter<NotificationAdapter.MyViewHolder>(), CustomClickListner {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

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
        holder.notificationListitemBinding.descriptionNotificationTv.text = notificationResponseData.data[position].description
        Glide.with(activity!!).load(notificationResponseData.data[position].image)
                .into(holder.notificationListitemBinding.notificationImg)
    }


    inner class MyViewHolder(itemView: NotificationListitemBinding) : RecyclerView.ViewHolder(itemView.root) {

        val notificationListitemBinding = itemView

        fun bind() {

        }


    }

    override fun onListClickListner() {

        Log.d("currentadapter", "onListClickListener")
    }

}