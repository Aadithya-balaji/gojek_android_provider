package com.xjek.provider.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.xjek.provider.R
import com.xjek.provider.databinding.TransactionStatusListBinding

class TransactionStatusListAdapter(activity: FragmentActivity?) : RecyclerView.Adapter<TransactionStatusListAdapter.MyViewHolder>() {

    val activity = activity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val inflate = DataBindingUtil.inflate<TransactionStatusListBinding>(LayoutInflater.from(parent.context), R.layout.transaction_status_list, parent, false)
        return MyViewHolder(inflate)
    }

    override fun getItemCount(): Int = 3

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()
    }


    inner class MyViewHolder(itemView: TransactionStatusListBinding) : RecyclerView.ViewHolder(itemView.root) {

        val currentOderItemlistBinding = itemView

        fun bind() {

        }


    }


}