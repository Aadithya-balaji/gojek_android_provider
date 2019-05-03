package com.xjek.xuberservice.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.xjek.xuberservice.R
import com.xjek.xuberservice.databinding.RowReasonBinding
import com.xjek.xuberservice.interfaces.CustomClickListner
import com.xjek.xuberservice.model.ReasonModel

class ReasonAdapter(context: Context,var customClickListner: CustomClickListner, var mReasonList: List<ReasonModel.ResponseData>) : RecyclerView.Adapter<ReasonAdapter.MyViewHolder>() {

    init {
        val mContext:Context=context
        val mCustomClickListner=customClickListner
        val mReasonlist=mReasonList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflate = DataBindingUtil.inflate<RowReasonBinding>(LayoutInflater.from(parent.context), R.layout.row_reason, parent, false)
        return MyViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return  mReasonList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val responseData=mReasonList.get(position)
        holder.currentOderItemlistBinding.tvReaon.setText(responseData.reason)
        holder.currentOderItemlistBinding.tvReaon.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                customClickListner.onListClickListner(position)
            }
        })
    }

    inner class MyViewHolder(itemView: RowReasonBinding) : RecyclerView.ViewHolder(itemView.root) {
        val currentOderItemlistBinding = itemView
    }
}