package com.xjek.provider.views.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.xjek.foodservice.view.FoodLiveTaskServiceFlow
import com.xjek.provider.R
import com.xjek.provider.databinding.RowFoodproviderItemBinding

class FoodProviderAdapter(activity: FragmentActivity?) : RecyclerView.Adapter<FoodProviderAdapter.MyViewHolder>() {
    val activity = activity
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val inflate = DataBindingUtil.inflate<RowFoodproviderItemBinding>(LayoutInflater.from(parent.context), R.layout.row_foodprovider_item, parent, false)
        return MyViewHolder(inflate)

    }

    override fun getItemCount(): Int = 1
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.rowFoodproviderItemBinding.tvApprove.setOnClickListener(){
            activity!!.startActivity(Intent(activity, FoodLiveTaskServiceFlow::class.java))
        }

    }

    class MyViewHolder(itemView: RowFoodproviderItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        val rowFoodproviderItemBinding = itemView
    }
}