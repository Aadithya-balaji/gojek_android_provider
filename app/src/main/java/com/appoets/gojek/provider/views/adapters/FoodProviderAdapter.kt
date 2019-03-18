package com.appoets.gojek.provider.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.RowFoodproviderItemBinding

class FoodProviderAdapter(activity: FragmentActivity?) : RecyclerView.Adapter<FoodProviderAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val inflate = DataBindingUtil.inflate<RowFoodproviderItemBinding>(LayoutInflater.from(parent.context), R.layout.row_foodprovider_item, parent, false)
        return MyViewHolder(inflate)

    }

    override fun getItemCount(): Int = 1
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


    }

    class MyViewHolder(itemView: RowFoodproviderItemBinding) : RecyclerView.ViewHolder(itemView.root) {


    }
}