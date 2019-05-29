package com.gox.foodservice.adapter

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.foodservice.R
import com.gox.foodservice.databinding.RowOderItemlistBinding
import com.gox.foodservice.model.Item

class OrderItemListAdapter(var activity: FragmentActivity?, var itemList: List<Item>) : RecyclerView.Adapter<OrderItemListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflate = DataBindingUtil.inflate<RowOderItemlistBinding>(LayoutInflater.from(parent.context), R.layout.row_oder_itemlist, parent, false)
        return MyViewHolder(inflate)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()
        holder.currentOderItemlistBinding.orderItemName.text = itemList[position].product.item_name + " x " + itemList[position].quantity
        holder.currentOderItemlistBinding.orderItemCostTv.text = activity!!.readPreferences<String>(PreferencesKey.CURRENCY_SYMBOL) + itemList[position].product.item_price

        if (itemList[position].cartaddon.isNotEmpty()) {
            holder.currentOderItemlistBinding.addonItemCostTv.visibility = VISIBLE
            holder.currentOderItemlistBinding.addonItemName.visibility = VISIBLE
            holder.currentOderItemlistBinding.addonItemName.text = itemList[position].cartaddon[0].addon_name + " x 1"
            holder.currentOderItemlistBinding.addonItemCostTv.text = activity!!.readPreferences<String>(PreferencesKey.CURRENCY_SYMBOL) + itemList[position].cartaddon[0].addon_price
        } else {
            holder.currentOderItemlistBinding.addonItemCostTv.visibility = GONE
            holder.currentOderItemlistBinding.addonItemName.visibility = GONE
        }
    }

    inner class MyViewHolder(itemView: RowOderItemlistBinding) : RecyclerView.ViewHolder(itemView.root) {
        val currentOderItemlistBinding = itemView
        fun bind() {
        }
    }
}
