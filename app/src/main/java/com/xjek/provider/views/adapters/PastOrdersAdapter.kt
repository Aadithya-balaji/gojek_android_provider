package com.xjek.xjek.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.xjek.provider.R
import com.xjek.provider.databinding.PastOderItemlistBinding

class PastOrdersAdapter(activity: FragmentActivity?) : RecyclerView.Adapter<PastOrdersAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val inflate = DataBindingUtil.inflate<PastOderItemlistBinding>(LayoutInflater.from(parent.context), R.layout.past_oder_itemlist, parent, false)
        return MyViewHolder(inflate)
    }

    override fun getItemCount(): Int = 3

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()
    }


    inner class MyViewHolder(itemView: PastOderItemlistBinding) : RecyclerView.ViewHolder(itemView.root) {


        fun bind() {

        }


    }


}