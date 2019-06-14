package com.gox.taxiservice.views.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.gox.taxiservice.R
import com.gox.taxiservice.databinding.ItemTaxiReasonBinding
import com.gox.taxiservice.interfaces.CustomClickListener
import com.gox.taxiservice.model.ReasonModel

class TaxiReasonAdapter(var mListener: CustomClickListener,
                        var mReasonList: List<ReasonModel.ResponseData>)
    : RecyclerView.Adapter<TaxiReasonAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(DataBindingUtil.inflate
        (LayoutInflater.from(parent.context), R.layout.item_taxi_reason, parent, false))
    }

    override fun getItemCount() = mReasonList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val responseData = mReasonList[position]
        holder.currentOderItemListBinding.tvReason.text = responseData.reason
        holder.currentOderItemListBinding.tvReason.setOnClickListener { mListener.onListClickListener(position) }
    }

    inner class MyViewHolder(itemView: ItemTaxiReasonBinding) : RecyclerView.ViewHolder(itemView.root) {
        val currentOderItemListBinding = itemView
    }
}