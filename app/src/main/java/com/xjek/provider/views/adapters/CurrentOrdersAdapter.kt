package com.xjek.provider.views.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.xjek.provider.R
import com.xjek.provider.databinding.CurrentOderItemlistBinding
import com.xjek.provider.interfaces.CustomClickListner
import com.xjek.provider.model.TransportHistory
import com.xjek.provider.utils.CommanMethods
import com.xjek.taxiservice.views.main.TaxiDashboardActivity

class CurrentOrdersAdapter(activity: FragmentActivity?, val transportCurentHistory: List<TransportHistory.TransportResponseData.Transport>)
    : RecyclerView.Adapter<CurrentOrdersAdapter.MyViewHolder>(), CustomClickListner {

    val activity = activity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val inflate = DataBindingUtil.inflate<CurrentOderItemlistBinding>(LayoutInflater.from(parent.context),
                R.layout.current_oder_itemlist, parent, false)
        return MyViewHolder(inflate)
    }

    override fun getItemCount(): Int = transportCurentHistory.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()
        holder.currentOderItemlistBinding.dateCurrentListTv.text = (CommanMethods.getLocalTimeStamp(transportCurentHistory[position]
                .assigned_at, "Req_Date_Month") + "")
        holder.currentOderItemlistBinding.timeCurrentListTv.text = (CommanMethods.getLocalTimeStamp(transportCurentHistory[position]
                .assigned_at, "Req_time") + "")
        holder.currentOderItemlistBinding.titleCurrentListTv.text = (transportCurentHistory[position].booking_id)
        holder.currentOderItemlistBinding.orderedItemTv.text = transportCurentHistory[position].ride.vehicle_name

        holder.currentOderItemlistBinding.currentorderitemListLayout.setOnClickListener({

            val intent = Intent(activity, TaxiDashboardActivity::class.java)
            intent.putExtra("menuId", transportCurentHistory[position].ride.id)
            activity!!.startActivity(intent)
        })
    }


    inner class MyViewHolder(itemView: CurrentOderItemlistBinding) : RecyclerView.ViewHolder(itemView.root) {

        val currentOderItemlistBinding = itemView


        fun bind() {

        }


    }

    override fun onListClickListner() {

        /*     val intent = Intent(activity, TaxiMainActivity::class.java)
             intent.putExtra("menuId", transportCurentHistory[])
             activity!!.startActivity(intent)*/

    }

}