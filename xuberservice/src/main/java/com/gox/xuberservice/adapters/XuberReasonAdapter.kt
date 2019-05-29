package com.gox.xuberservice.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.gox.xuberservice.R
import com.gox.xuberservice.databinding.ItemXuberReasonBinding
import com.gox.xuberservice.interfaces.CustomClickListner
import com.gox.xuberservice.model.ReasonModel

class XuberReasonAdapter(context: Context, var customClickListner: CustomClickListner, var mReasonList: List<ReasonModel.ResponseData>)
    : RecyclerView.Adapter<XuberReasonAdapter.MyViewHolder>() {

    init {
        val mContext: Context = context
        val mCustomClickListner = customClickListner
        val mReasonlist = mReasonList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflate = DataBindingUtil.inflate<ItemXuberReasonBinding>(LayoutInflater.from(parent.context), R.layout.item_xuber_reason, parent, false)
        return MyViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return mReasonList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val responseData = mReasonList.get(position)
        holder.currentOderItemlistBinding.tvReaon.setText(responseData.reason)
        holder.currentOderItemlistBinding.tvReaon.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                customClickListner.onListClickListner(position)
            }
        })
    }

    inner class MyViewHolder(itemView: ItemXuberReasonBinding) : RecyclerView.ViewHolder(itemView.root) {
        val currentOderItemlistBinding = itemView
    }
}