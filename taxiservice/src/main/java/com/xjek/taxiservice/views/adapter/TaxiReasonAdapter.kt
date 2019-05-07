package com.xjek.taxiservice.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.xjek.taxiservice.R
import com.xjek.taxiservice.databinding.ItemTaxiReasonBinding
import com.xjek.taxiservice.interfaces.CustomClickListener
import com.xjek.taxiservice.model.ReasonModel

class TaxiReasonAdapter(context: Context, var customClickListener: CustomClickListener, var mReasonList: List<ReasonModel.ResponseData>)
    : RecyclerView.Adapter<TaxiReasonAdapter.MyViewHolder>() {

    init {
        val mContext: Context = context
        val mCustomClickListener = customClickListener
        val mReasonList = mReasonList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(DataBindingUtil.inflate
        (LayoutInflater.from(parent.context), R.layout.item_taxi_reason, parent, false))
    }

    override fun getItemCount(): Int {
        return mReasonList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val responseData = mReasonList[position]
        holder.currentOderItemListBinding.tvReason.text = responseData.reason
        holder.currentOderItemListBinding.tvReason.setOnClickListener { customClickListener.onListClickListener(position) }
    }

    inner class MyViewHolder(itemView: ItemTaxiReasonBinding) : RecyclerView.ViewHolder(itemView.root) {
        val currentOderItemListBinding = itemView
    }
}