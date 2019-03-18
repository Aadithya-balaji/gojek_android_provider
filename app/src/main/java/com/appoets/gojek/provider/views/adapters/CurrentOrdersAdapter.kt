package com.appoets.xjek.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.appoets.xjek.R
import com.appoets.xjek.databinding.CurrentOderItemlistBinding
import com.appoets.xjek.ui.currentordersdetails.CurrentOrdersDetailActivity

class CurrentOrdersAdapter(activity: FragmentActivity?) : RecyclerView.Adapter<CurrentOrdersAdapter.MyViewHolder>(), CustomClickListner {

    val activity = activity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val inflate = DataBindingUtil.inflate<CurrentOderItemlistBinding>(LayoutInflater.from(parent.context), R.layout.current_oder_itemlist, parent, false)
        return MyViewHolder(inflate)
    }

    override fun getItemCount(): Int = 3

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()
        holder.currentOderItemlistBinding.setItemClickListener(this);
    }


    inner class MyViewHolder(itemView: CurrentOderItemlistBinding) : RecyclerView.ViewHolder(itemView.root) {

        val currentOderItemlistBinding = itemView

        fun bind() {

        }


    }

    override fun onListClickListner() {

        val intent = Intent(activity, CurrentOrdersDetailActivity::class.java)
        activity!!.startActivity(intent)
        Log.d("currentadapter", "onListClickListner")

    }

}