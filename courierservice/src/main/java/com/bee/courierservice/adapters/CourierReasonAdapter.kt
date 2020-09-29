package com.bee.courierservice.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bee.courierservice.R
import com.bee.courierservice.databinding.ItemCourierReasonBinding
import com.bee.courierservice.interfaces.CustomClickListener
import com.bee.courierservice.model.ReasonModel

class CourierReasonAdapter(var mListener: CustomClickListener, var mReasonList: List<ReasonModel.ResponseData>)
    : RecyclerView.Adapter<CourierReasonAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            MyViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                    R.layout.item_courier_reason, parent, false))

    override fun getItemCount() = mReasonList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val responseData = mReasonList.get(position)
        holder.mBinding.tvReaon.text = responseData.reason
        holder.mBinding.tvReaon.setOnClickListener { mListener.onListClickListener(position) }
    }

    inner class MyViewHolder(itemView: ItemCourierReasonBinding) : RecyclerView.ViewHolder(itemView.root) {
        val mBinding = itemView
    }
}