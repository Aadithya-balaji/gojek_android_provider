package com.gox.partner.views.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.gox.partner.R
import com.gox.partner.databinding.PastOrderItemlistBinding
import com.gox.partner.interfaces.CustomClickListener
import com.gox.partner.models.HistoryModel
import com.gox.partner.utils.CommonMethods
import com.gox.partner.views.history_details.HistoryDetailActivity

class PastOrdersAdapter(fragmentActivity: FragmentActivity?,
                        transPortList: List<HistoryModel.ResponseData.Transport>,
                        orderList: List<HistoryModel.ResponseData.Order>,
                        serviceList: List<HistoryModel.ResponseData.Service>,
                        serviceType: String)
    : RecyclerView.Adapter<PastOrdersAdapter.MyViewHolder>(), CustomClickListener {

    private var transPortList: List<HistoryModel.ResponseData.Transport>? = null
    private var orderList: List<HistoryModel.ResponseData.Order>? = null
    private var serviceList: List<HistoryModel.ResponseData.Service>? = null
    private var serviceType: String? = null
    private var activity: FragmentActivity? = null
    private var selectedId: String? = ""

    init {
        this.transPortList = transPortList
        this.orderList = orderList
        this.serviceList = serviceList
        this.activity = fragmentActivity!!
        this.serviceType = serviceType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.past_order_itemlist, parent, false))
    }

    override fun getItemCount(): Int {
        return when {
            serviceType.equals("transport") -> transPortList!!.size
            serviceType.equals("service") -> serviceList!!.size
            else -> orderList!!.size
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (serviceType.equals("transport")) {
            val transPortModel = transPortList!!.get(position)
            holder.mBinding.tvTitlePastItem.text = transPortModel.booking_id.toString()
            holder.mBinding.tvDatePastItem.text =
                    CommonMethods.getLocalTimeStamp(transPortModel.assigned_at.toString(), "Req_Date_Month")
            holder.mBinding.tvTimePastItem.text =
                    CommonMethods.getLocalTimeStamp(transPortModel.assigned_at.toString(), "Req_time")
            holder.mBinding.tvPastItem.text = String.format(activity!!.getString(R.string.history_vechile_name),
                    transPortModel.ride!!.vehicle_name!!.toString(),
                    transPortModel.provider_vehicle!!.vehicle_no.toString())
            holder.mBinding.tvRatingPastItem.text = String.format(activity!!.getString(R.string.transaction_amount),
                    transPortModel.user!!.rating!!.toDouble())
            if (transPortModel.status.equals("COMPLETED")) {
                holder.mBinding.tvStatusPastItem.background =
                        ContextCompat.getDrawable(activity!!, R.drawable.bg_past_item_completed)
                holder.mBinding.tvStatusPastItem.setTextColor(
                        ContextCompat.getColor(activity!!, R.color.text_on_ride))
            } else {
                holder.mBinding.tvStatusPastItem.background =
                        ContextCompat.getDrawable(activity!!, R.drawable.bg_past_item_cancelled)
                holder.mBinding.tvStatusPastItem.setTextColor(
                        ContextCompat.getColor(activity!!, R.color.past_item_cancelled))
            }
        } else if (serviceType!! == "order") {
            val orderedItem = orderList!!.get(position)
            holder.mBinding.tvTitlePastItem.text = orderedItem.store_order_invoice_id.toString()
            holder.mBinding.tvDatePastItem.text =
                    CommonMethods.getLocalTimeStamp(orderedItem.created_at.toString(), "Req_Date_Month")
            holder.mBinding.tvTimePastItem.text =
                    CommonMethods.getLocalTimeStamp(orderedItem.created_at.toString(), "Req_time")
            if (orderedItem.status.equals("COMPLETED")) {
                holder.mBinding.tvStatusPastItem.background =
                        ContextCompat.getDrawable(activity!!, R.drawable.bg_past_item_completed)
                holder.mBinding.tvStatusPastItem.setTextColor(
                        ContextCompat.getColor(activity!!, R.color.text_on_ride))
            } else {
                holder.mBinding.tvStatusPastItem.background =
                        ContextCompat.getDrawable(activity!!, R.drawable.bg_past_item_cancelled)
                holder.mBinding.tvStatusPastItem.setTextColor(
                        ContextCompat.getColor(activity!!, R.color.past_item_cancelled))
            }
        } else {
            val serviceItem = serviceList!!.get(position)
            holder.mBinding.tvTitlePastItem.text = serviceItem.booking_id.toString()
            holder.mBinding.tvDatePastItem.text =
                    CommonMethods.getLocalTimeStamp(serviceItem.assigned_at.toString(), "Req_Date_Month")
            holder.mBinding.tvTimePastItem.text =
                    CommonMethods.getLocalTimeStamp(serviceItem.assigned_at.toString(), "Req_time")
            holder.mBinding.tvPastItem.text = serviceItem.service!!.service_name.toString()
            holder.mBinding.tvRatingPastItem.text = serviceItem.user!!.rating
            if (serviceItem.status.equals("COMPLETED")) {
                holder.mBinding.tvStatusPastItem.background =
                        ContextCompat.getDrawable(activity!!, R.drawable.bg_past_item_completed)
                holder.mBinding.tvStatusPastItem.setTextColor(
                        ContextCompat.getColor(activity!!, R.color.text_on_ride))
            } else {
                holder.mBinding.tvStatusPastItem.background =
                        ContextCompat.getDrawable(activity!!, R.drawable.bg_past_item_cancelled)
                holder.mBinding.tvStatusPastItem.setTextColor(
                        ContextCompat.getColor(activity!!, R.color.past_item_cancelled))
            }
        }

        holder.mBinding.itemClickListener = object : CustomClickListener {
            override fun onListClickListener() {
                selectedId = when {
                    serviceType.equals("transport") -> transPortList!![holder.adapterPosition].id.toString()
                    serviceType.equals("order") -> orderList!!.get(holder.adapterPosition).id.toString()
                    else -> serviceList!!.get(holder.adapterPosition).id.toString()
                }
                val intent = Intent(activity, HistoryDetailActivity::class.java)
                intent.putExtra("selected_trip_id", selectedId)
                intent.putExtra("history_type", "past")
                intent.putExtra("serviceType", serviceType)
                activity!!.startActivity(intent)
            }
        }
    }

    inner class MyViewHolder(itemView: PastOrderItemlistBinding) :
            RecyclerView.ViewHolder(itemView.root) {
        val mBinding = itemView
    }

    override fun onListClickListener() {
        Log.d("currentadapter", "onListClickListener")
    }
}