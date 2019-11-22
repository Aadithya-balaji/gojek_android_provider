package com.gox.partner.views.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.gox.base.data.Constants
import com.gox.partner.R
import com.gox.partner.databinding.PastOderItemlistBinding
import com.gox.partner.models.TransportResponseData
import com.gox.partner.utils.CommonMethods
import com.gox.partner.views.history_details.HistoryDetailActivity


class PastOrdersAdapter(val activity: FragmentActivity?, val transportHistory: TransportResponseData
                        , val servicetype: String) :
        RecyclerView.Adapter<PastOrdersAdapter.MyViewHolder>(), CustomClickListner {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflate = DataBindingUtil.inflate<PastOderItemlistBinding>(LayoutInflater.from(parent.context),
                R.layout.past_oder_itemlist, parent, false)
        return MyViewHolder(inflate)
    }

    override fun getItemCount(): Int =
            when (servicetype) {
                Constants.ModuleTypes.TRANSPORT -> transportHistory.transport.size
                Constants.ModuleTypes.SERVICE -> transportHistory.service.size
                Constants.ModuleTypes.ORDER -> transportHistory.order.size
                else -> 0
            }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()

        when (servicetype) {
            Constants.ModuleTypes.TRANSPORT -> {
                updateHistoryUi(holder, transportHistory.transport[position].ride?.vehicle_name!!
                        , transportHistory.transport[position].status.equals("Completed", true)
                        , transportHistory.transport[position].booking_id
                        , transportHistory.transport[position].rating?.provider_rating?:0.0
                        , (CommonMethods.getLocalTimeStamp(transportHistory.transport[position].assigned_at, "Req_time") + "")
                        , (CommonMethods.getLocalTimeStamp(transportHistory.transport[position].assigned_at, "Req_Date_Month") + ""))

                holder.pastOderItemlistBinding.itemClickListener = object : CustomClickListner {
                    override fun onListClickListner() {
                        if (!transportHistory.transport[position].status.equals("cancelled", true)) {
                            val intent = Intent(activity, HistoryDetailActivity::class.java)
                            intent.putExtra("selected_trip_id", transportHistory.transport[position].id.toString())
                            intent.putExtra("history_type", "past")
                            intent.putExtra("servicetype", servicetype)
                            activity!!.startActivity(intent)
                        }
                    }
                }
            }
            Constants.ModuleTypes.SERVICE -> {
                updateHistoryUi(holder, transportHistory.service[position].service?.service_name.toString()!!
                        , transportHistory.service[position].status.equals("Completed", true)
                        , transportHistory.service[position].booking_id!!
                        , transportHistory.service[position].rating?.provider_rating?:0.0
                        , (CommonMethods.getLocalTimeStamp(transportHistory.service[position].assigned_at!!, "Req_time") + "")
                        , (CommonMethods.getLocalTimeStamp(transportHistory.service[position].assigned_at!!, "Req_Date_Month") + ""))

                holder.pastOderItemlistBinding.itemClickListener = object : CustomClickListner {
                    override fun onListClickListner() {
                        if (!transportHistory.service[position].status.equals("cancelled", true)) {
                            val intent = Intent(activity, HistoryDetailActivity::class.java)
                            intent.putExtra("selected_trip_id", transportHistory.service[position].id.toString())
                            intent.putExtra("history_type", "past")
                            intent.putExtra("servicetype", servicetype)
                            activity!!.startActivity(intent)
                        }
                    }
                }
            }

            Constants.ModuleTypes.ORDER -> {
                updateHistoryUi(holder, transportHistory.order[position].pickup?.store_name.toString()!!
                        , transportHistory.order[position].status.equals("Completed", true)
                        , transportHistory.order[position].store_order_invoice_id!!
                        , transportHistory.order[position].rating?.provider_rating?:0.0
                        , (CommonMethods.getLocalTimeStamp(transportHistory.order[position].created_at!!, "Req_time") + "")
                        , (CommonMethods.getLocalTimeStamp(transportHistory.order[position].created_at!!, "Req_Date_Month") + ""))


                holder.pastOderItemlistBinding.itemClickListener = object : CustomClickListner {
                    override fun onListClickListner() {
                        if (!transportHistory.order[position].status.equals("cancelled", true)) {
                            val intent = Intent(activity, HistoryDetailActivity::class.java)
                            intent.putExtra("selected_trip_id", transportHistory.order[position].id.toString())
                            intent.putExtra("history_type", "past")
                            intent.putExtra("servicetype", servicetype)
                            activity!!.startActivity(intent)
                        }
                    }
                }

            }
        }
    }

    private fun updateHistoryUi(holder: MyViewHolder, order: String, status: Boolean, booking_id: String, rating: Double
                                , req_time: String, req_month: String) {
        holder.pastOderItemlistBinding.titlePastListTv.text = booking_id
        holder.pastOderItemlistBinding.ratingPastTv.text = String.format("%.2f", rating)
        holder.pastOderItemlistBinding.datePastListTv.text = req_month
        holder.pastOderItemlistBinding.timePastListTv.text = req_time
        holder.pastOderItemlistBinding.orderedItemTv.text = order

        if (status) {
            holder.pastOderItemlistBinding.statusPastTv.background = ContextCompat.getDrawable(activity as Context,
                    R.drawable.custom_round_corner_completd)
            holder.pastOderItemlistBinding.statusPastTv.setTextColor(ContextCompat.getColor(activity as Context,
                    R.color.completed_text))
            holder.pastOderItemlistBinding.statusPastTv.text = activity.getString(R.string.completed)
        } else {
            holder.pastOderItemlistBinding.statusPastTv.background = ContextCompat.getDrawable(activity as Context,
                    R.drawable.custom_round_corner_cancled)
            holder.pastOderItemlistBinding.statusPastTv.setTextColor(ContextCompat.getColor(activity as Context,
                    R.color.red))
            holder.pastOderItemlistBinding.statusPastTv.text = activity.getString(R.string.cancelled)
        }
    }

    inner class MyViewHolder(itemView: PastOderItemlistBinding) :
            RecyclerView.ViewHolder(itemView.root) {
        val pastOderItemlistBinding = itemView
        fun bind() {
        }
    }

    override fun onListClickListner() {
        Log.d("currentadapter", "onListClickListner")
    }
}