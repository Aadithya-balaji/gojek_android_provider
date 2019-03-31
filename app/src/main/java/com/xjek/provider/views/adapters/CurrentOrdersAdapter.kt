package com.xjek.xjek.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.xjek.provider.R
import com.xjek.provider.databinding.CurrentOderItemlistBinding

class CurrentOrdersAdapter(activity: FragmentActivity?) : RecyclerView.Adapter<CurrentOrdersAdapter.MyViewHolder>() {

    val activity = activity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val inflate = DataBindingUtil.inflate<CurrentOderItemlistBinding>(LayoutInflater.from(parent.context), R.layout.current_oder_itemlist, parent, false)
        return MyViewHolder(inflate)
    }

    override fun getItemCount(): Int = 3

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()
    }


    inner class MyViewHolder(itemView: CurrentOderItemlistBinding) : RecyclerView.ViewHolder(itemView.root) {

        val currentOderItemlistBinding = itemView

        fun bind() {

        }


    }

}