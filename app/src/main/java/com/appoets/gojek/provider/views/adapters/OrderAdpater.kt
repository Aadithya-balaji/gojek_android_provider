package com.appoets.gojek.provider.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appoets.gojek.provider.R


class OrderAdpater (): RecyclerView.Adapter<OrderAdpater.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_order_item, parent, false)
        return OrderViewHolder(v)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
    }

    inner class OrderViewHolder(orderItemView: View) : RecyclerView.ViewHolder(orderItemView) {
        val tvOrderDate = orderItemView.findViewById(R.id.tv_order_date) as TextView
        val tvOrderTime = orderItemView.findViewById(R.id.tv_order_time) as TextView
        val tvOrderName = orderItemView.findViewById(R.id.tv_order_name) as TextView
        val  tvOrderStatus=orderItemView.findViewById(R.id.tv_order_status) as TextView
        val tvOrderTrack=orderItemView.findViewById(R.id.tv_order_track) as TextView
        val tvOrdercall=orderItemView.findViewById(R.id.tv_order_call) as TextView

    }
}