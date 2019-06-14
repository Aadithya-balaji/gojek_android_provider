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
import com.gox.partner.interfaces.CustomClickListener
import com.gox.partner.models.NotificationResponse

class NotificationAdapter(val activity: FragmentActivity?, private val notificationResponseData: NotificationResponse.ResponseData.Notification) : RecyclerView.Adapter<NotificationAdapter.MyViewHolder>(), CustomClickListener {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        return MyViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context)
                , R.layout.notification_listitem, parent, false))
    }

    override fun getItemCount(): Int = notificationResponseData.data.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()
        holder.mBinding.notificationTimeTv.text =
                getTimeDifference(notificationResponseData.data[position].created_at)
        holder.mBinding.titlenotificationListTv.text = notificationResponseData.data[position].title
        holder.mBinding.descriptionNotificationTv.text = notificationResponseData.data[position].descriptions
        Glide.with(activity!!).load(notificationResponseData.data[position].image)
                .into(holder.mBinding.notificationImg)
    }

    inner class MyViewHolder(itemView: NotificationListitemBinding) : RecyclerView.ViewHolder(itemView.root) {

        val mBinding = itemView

        fun bind() {
            mBinding.descriptionNotificationTv.setShowingLine(3)
            mBinding.descriptionNotificationTv.addShowMoreText("show more")
            mBinding.descriptionNotificationTv.addShowLessText("show less")
            mBinding.descriptionNotificationTv.setShowMoreColor(ContextCompat.getColor(context, R.color.colorBasePrimary))
            mBinding.descriptionNotificationTv.setShowLessTextColor(ContextCompat.getColor(context, R.color.colorBasePrimary))
        }
    }

    override fun onListClickListener() {
        Log.d("currentadapter", "onListClickListener")
    }

}