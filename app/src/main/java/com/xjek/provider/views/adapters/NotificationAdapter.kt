package com.xjek.provider.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xjek.provider.R

class NotificationAdapter:RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val v:View=LayoutInflater.from(parent.context).inflate(R.layout.row_notification, parent, false)
        return NotificationViewHolder(v)
    }

    override fun getItemCount(): Int {
        return  1
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {

    }

    inner class  NotificationViewHolder(itemview: View):RecyclerView.ViewHolder(itemview){
        val tvValidDate=itemview.findViewById(R.id.tv_notification_date) as TextView
        val tvServiceType=itemView.findViewById(R.id.tv_label_notification) as TextView
        val tvService=itemView.findViewById(R.id.tv_notification_service) as TextView

    }
}
