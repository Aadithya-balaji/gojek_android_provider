package com.xjek.provider.views.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.xjek.base.utils.CommonMethods
import com.xjek.provider.R
import com.xjek.provider.databinding.PastOrderItemlistBinding
import com.xjek.provider.interfaces.CustomClickListner
import com.xjek.provider.models.HistoryModel
import com.xjek.provider.utils.CommanMethods
import com.xjek.provider.views.history_details.HistoryDetailActivity

class PastOrdersAdapter( fragmentActivity:FragmentActivity?, transPortList: List<HistoryModel.ResponseData.Transport>, orderList: List<HistoryModel.ResponseData.Order>, serviceList: List<HistoryModel.ResponseData.Service>, serviceType: String) : RecyclerView.Adapter<PastOrdersAdapter.MyViewHolder>(), CustomClickListner {

    private var transPortList: List<HistoryModel.ResponseData.Transport>? = null
    private var orderList: List<HistoryModel.ResponseData.Order>? = null
    private var serviceList: List<HistoryModel.ResponseData.Service>? = null
    private var serviceType: String? = null
    private  lateinit var  activity: FragmentActivity
    private  var selectedId:String?=""

    init {
        this.transPortList = transPortList
        this.orderList = orderList
        this.serviceList = serviceList
        this.activity=fragmentActivity!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflate = DataBindingUtil.inflate<PastOrderItemlistBinding>(LayoutInflater.from(parent.context),
                R.layout.past_order_itemlist, parent, false)
        return MyViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        if (serviceType.equals("transport")) {
            return transPortList!!.size
        } else if (serviceType.equals("service")) {
            return serviceList!!.size
        } else {
            return orderList!!.size
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if(serviceType.equals("transport")){
            val transPortModel=transPortList!!.get(position)
            holder.pastOderItemlistBinding.tvTitlePastItem.text=transPortModel.booking_id.toString()
            holder.pastOderItemlistBinding.tvDatePastItem.text=CommanMethods.getLocalTimeStamp(transPortModel.assigned_at.toString(),"Req_Date_Month")
            holder.pastOderItemlistBinding.tvTimePastItem.text=CommanMethods.getLocalTimeStamp(transPortModel.assigned_at.toString(),"Req_time")
            holder.pastOderItemlistBinding.tvPastItem.text=String.format(activity.getString(R.string.history_vechile_name),transPortModel.ride!!.vehicle_name!!.toString(),transPortModel.provider_vehicle!!.vehicle_no.toString())
            holder.pastOderItemlistBinding.tvRatingPastItem.text=String.format(activity.getString(R.string.transaction_amount),transPortModel.user!!.rating!!.toDouble())
            if(transPortModel.status.equals("COMPLETED")){
                  holder.pastOderItemlistBinding.tvStatusPastItem.background=ContextCompat.getDrawable(activity,R.drawable.bg_past_item_completed)
                  holder.pastOderItemlistBinding.tvStatusPastItem.setTextColor(ContextCompat.getColor(activity,R.color.text_on_ride))
            }else{
                holder.pastOderItemlistBinding.tvStatusPastItem.background=ContextCompat.getDrawable(activity,R.drawable.bg_past_item_cancelled)
                holder.pastOderItemlistBinding.tvStatusPastItem.setTextColor(ContextCompat.getColor(activity,R.color.past_item_cancelled))
            }

        }else if(serviceType!!.equals("service")){
            val orderedItem=orderList!!.get(position)
            holder.pastOderItemlistBinding.tvTitlePastItem.text=orderedItem.store_order_invoice_id.toString()
           holder.pastOderItemlistBinding.tvDatePastItem.text=CommanMethods.getLocalTimeStamp(orderedItem.created_at.toString(),"Req_Date_Month")
            holder.pastOderItemlistBinding.tvTimePastItem.text=CommanMethods.getLocalTimeStamp(orderedItem.created_at.toString(),"Req_time")
          //  holder.pastOderItemlistBinding.tvPastItem.text=String.format(activity.getString(R.string.history_vechile_name),orderedItem.ride!!.vehicle_name!!.toString(),transPortModel.provider_vehicle!!.vehicle_no.toString())
           // holder.pastOderItemlistBinding.tvRatingPastItem.text=String.format(activity.getString(R.string.transaction_amount),orderedItem.user!!.rating!!.toDouble())
            if(orderedItem.status.equals("COMPLETED")){
                holder.pastOderItemlistBinding.tvStatusPastItem.background=ContextCompat.getDrawable(activity,R.drawable.bg_past_item_completed)
                holder.pastOderItemlistBinding.tvStatusPastItem.setTextColor(ContextCompat.getColor(activity,R.color.text_on_ride))
            }else{
                holder.pastOderItemlistBinding.tvStatusPastItem.background=ContextCompat.getDrawable(activity,R.drawable.bg_past_item_cancelled)
                holder.pastOderItemlistBinding.tvStatusPastItem.setTextColor(ContextCompat.getColor(activity,R.color.past_item_cancelled))
            }
        }else {
            val serviceItem=serviceList!!.get(position)
            holder.pastOderItemlistBinding.tvTitlePastItem.text=serviceItem.booking_id.toString()
            holder.pastOderItemlistBinding.tvDatePastItem.text=CommanMethods.getLocalTimeStamp(serviceItem.assigned_at.toString(),"Req_Date_Month")
            holder.pastOderItemlistBinding.tvTimePastItem.text=CommanMethods.getLocalTimeStamp(serviceItem.assigned_at.toString(),"Req_time")
            if(serviceItem.status.equals("COMPLETED")){
                holder.pastOderItemlistBinding.tvStatusPastItem.background=ContextCompat.getDrawable(activity,R.drawable.bg_past_item_completed)
                holder.pastOderItemlistBinding.tvStatusPastItem.setTextColor(ContextCompat.getColor(activity,R.color.text_on_ride))
            }else{
                holder.pastOderItemlistBinding.tvStatusPastItem.background=ContextCompat.getDrawable(activity,R.drawable.bg_past_item_cancelled)
                holder.pastOderItemlistBinding.tvStatusPastItem.setTextColor(ContextCompat.getColor(activity,R.color.past_item_cancelled))
            }

            }

        holder.pastOderItemlistBinding.itemClickListener = object : CustomClickListner {
            override fun onListClickListner() {
                if(serviceType.equals("transport")){
                    selectedId=transPortList!!.get(holder.adapterPosition).id.toString()
                }else if(serviceType.equals("order")){
                    selectedId=orderList!!.get(holder.adapterPosition).id.toString()
                }else{
                    selectedId=serviceList!!.get(holder.adapterPosition).id.toString()
                }
                val intent = Intent(activity, HistoryDetailActivity::class.java)
                intent.putExtra("selected_trip_id",selectedId)
                intent.putExtra("history_type", "past")
                activity!!.startActivity(intent)
            }
        }
    }

    inner class MyViewHolder(itemView: PastOrderItemlistBinding) :
            RecyclerView.ViewHolder(itemView.root) {
        val pastOderItemlistBinding = itemView
    }

    override fun onListClickListner() {
        Log.d("currentadapter", "onListClickListener")
    }
}