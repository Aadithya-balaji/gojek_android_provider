package com.appoets.gojek.provider.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.TransactionListItemBinding

class TransactionListAdapter(activity: FragmentActivity?) : RecyclerView.Adapter<TransactionListAdapter.MyViewHolder>() {

    val activity = activity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val inflate = DataBindingUtil.inflate<TransactionListItemBinding>(LayoutInflater.from(parent.context), R.layout.transaction_list_item, parent, false)
        return MyViewHolder(inflate)
    }

    override fun getItemCount(): Int = 3

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()
    }


    inner class MyViewHolder(itemView: TransactionListItemBinding) : RecyclerView.ViewHolder(itemView.root) {

        val currentOderItemlistBinding = itemView

        fun bind() {

        }


    }


}