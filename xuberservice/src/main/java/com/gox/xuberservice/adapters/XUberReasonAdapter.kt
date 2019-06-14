package com.gox.xuberservice.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.gox.xuberservice.R
import com.gox.xuberservice.databinding.ItemXuberReasonBinding
import com.gox.xuberservice.interfaces.CustomClickListener
import com.gox.xuberservice.model.ReasonModel

class XUberReasonAdapter(var mListener: CustomClickListener, var mReasonList: List<ReasonModel.ResponseData>)
    : RecyclerView.Adapter<XUberReasonAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            MyViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                    R.layout.item_xuber_reason, parent, false))

    override fun getItemCount() = mReasonList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val responseData = mReasonList.get(position)
        holder.mBinding.tvReaon.text = responseData.reason
        holder.mBinding.tvReaon.setOnClickListener { mListener.onListClickListener(position) }
    }

    inner class MyViewHolder(itemView: ItemXuberReasonBinding) : RecyclerView.ViewHolder(itemView.root) {
        val mBinding = itemView
    }
}