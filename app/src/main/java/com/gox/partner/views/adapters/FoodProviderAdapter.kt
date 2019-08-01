package com.gox.partner.views.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.gox.foodservice.ui.dashboard.FoodieDashboardActivity
import com.gox.partner.R
import com.gox.partner.databinding.RowFoodproviderItemBinding

class FoodProviderAdapter(val activity: FragmentActivity?) : RecyclerView.Adapter<FoodProviderAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.row_foodprovider_item, parent, false))
    }

    override fun getItemCount(): Int = 1
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mBinding.tvApprove.setOnClickListener {
            activity!!.startActivity(Intent(activity, FoodieDashboardActivity::class.java))
        }
    }

    class MyViewHolder(itemView: RowFoodproviderItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        val mBinding = itemView
    }
}