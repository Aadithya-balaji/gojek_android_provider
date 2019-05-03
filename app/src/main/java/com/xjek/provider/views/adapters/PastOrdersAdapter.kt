package com.xjek.provider.views.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.xjek.provider.R
import com.xjek.provider.databinding.PastOrderItemlistBinding
import com.xjek.provider.interfaces.CustomClickListner
import com.xjek.provider.model.TransportHistory
import com.xjek.provider.utils.CommanMethods
import com.xjek.provider.views.history_details.HistoryDetailActivity

class PastOrdersAdapter(val activity: FragmentActivity?, val transportHistory: List<TransportHistory.TransportResponseData.Transport>) :
        RecyclerView.Adapter<PastOrdersAdapter.MyViewHolder>(),
        CustomClickListner {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflate = DataBindingUtil.inflate<PastOrderItemlistBinding>(LayoutInflater.from(parent.context),
                R.layout.past_order_itemlist, parent, false)
        return MyViewHolder(inflate)
    }

    override fun getItemCount(): Int = transportHistory.size
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()
        holder.pastOderItemlistBinding.titlePastListTv.text = (transportHistory[position].booking_id)
        holder.pastOderItemlistBinding.orderedItemTv.text = transportHistory[position].ride.vehicle_name
        holder.pastOderItemlistBinding.ratingPastTv.text = ("" + transportHistory[position].user.rating)
        holder.pastOderItemlistBinding.datePastListTv.text = (CommanMethods.getLocalTimeStamp(transportHistory[position]
                .assigned_at, "Req_Date_Month") + "")
        holder.pastOderItemlistBinding.timePastListTv.text = (CommanMethods.getLocalTimeStamp(transportHistory[position]
                .assigned_at, "Req_time") + "")
        if (transportHistory[position].status.equals("Completed", true)) {
            holder.pastOderItemlistBinding.statusPastTv.background = ContextCompat.getDrawable(activity as Context,
                    R.drawable.custom_round_corner_completd)
            holder.pastOderItemlistBinding.statusPastTv.setTextColor(ContextCompat.getColor(activity as Context,
                    R.color.schedule_blue_txt))
            holder.pastOderItemlistBinding.statusPastTv.text = transportHistory[position].status
        } else {
            holder.pastOderItemlistBinding.statusPastTv.text = transportHistory[position].status
        }
        holder.pastOderItemlistBinding.itemClickListener = object : CustomClickListner {
            override fun onListClickListner() {
                val intent = Intent(activity, HistoryDetailActivity::class.java)
                intent.putExtra("selected_trip_id", transportHistory[position].id)
                intent.putExtra("history_type", "past")
                activity!!.startActivity(intent)
            }
        }
    }

    inner class MyViewHolder(itemView: PastOrderItemlistBinding) :
            RecyclerView.ViewHolder(itemView.root) {
        val pastOderItemlistBinding = itemView
        fun bind() {
        }
    }

    override fun onListClickListner() {
        Log.d("currentadapter", "onListClickListner")
    }
}