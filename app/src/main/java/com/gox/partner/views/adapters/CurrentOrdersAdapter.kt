/*
package com.gox.partner.views.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.gox.partner.R
import com.gox.partner.databinding.CurrentOderItemlistBinding
import com.gox.partner.interfaces.CustomClickListener
import com.gox.partner.models.TransportHistory
import com.gox.partner.utils.CommonMethods
import com.gox.taxiservice.views.main.TaxiDashboardActivity

class CurrentOrdersAdapter(val activity: FragmentActivity?, private val transportCurrentHistory: List<TransportHistory.TransportResponseData.Transport>)
    : RecyclerView.Adapter<CurrentOrdersAdapter.MyViewHolder>(), CustomClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.current_oder_itemlist, parent, false))
    }

    override fun getItemCount(): Int = transportCurrentHistory.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()
        holder.mBinding.dateCurrentListTv.text =
                (CommonMethods.getLocalTimeStamp(transportCurrentHistory[position].assigned_at, "Req_Date_Month") + "")
        holder.mBinding.timeCurrentListTv.text =
                (CommonMethods.getLocalTimeStamp(transportCurrentHistory[position].assigned_at, "Req_time") + "")
        holder.mBinding.titleCurrentListTv.text = (transportCurrentHistory[position].booking_id)
        holder.mBinding.orderedItemTv.text = transportCurrentHistory[position].ride.vehicle_name

        holder.mBinding.currentorderitemListLayout.setOnClickListener {
            val intent = Intent(activity, TaxiDashboardActivity::class.java)
            intent.putExtra("menuId", transportCurrentHistory[position].ride.id)
            activity!!.startActivity(intent)
        }
    }

    inner class MyViewHolder(itemView: CurrentOderItemlistBinding) : RecyclerView.ViewHolder(itemView.root) {

        val mBinding = itemView
        fun bind() {

        }
    }

    override fun onListClickListener() {

    }

}*/
