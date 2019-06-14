package com.gox.partner.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.gox.partner.R
import com.gox.partner.databinding.TransactionStatusListBinding

class TransactionStatusListAdapter(val activity: FragmentActivity?)
    : RecyclerView.Adapter<TransactionStatusListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.transaction_status_list, parent, false))
    }

    override fun getItemCount() = 3

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()
    }

    inner class MyViewHolder(itemView: TransactionStatusListBinding) : RecyclerView.ViewHolder(itemView.root) {
        val mBinding = itemView
        fun bind() {
        }
    }

}