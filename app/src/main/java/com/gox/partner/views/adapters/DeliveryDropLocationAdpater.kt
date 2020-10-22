package com.gox.partner.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.gox.partner.R
import com.gox.partner.databinding.ListDeliveryDropLocationBinding
import com.gox.partner.models.HistoryDetailModel


class DeliveryDropLocationAdpater( val context: Context, val dropLocationList: List<HistoryDetailModel.ResponseData.Delivery.DeliveryData>) : RecyclerView.Adapter<DeliveryDropLocationAdpater.DropLocationViewHolder>() {

    private var selectedPosition: Int? = -1

    inner class DropLocationViewHolder(listDropLocationBinding: ListDeliveryDropLocationBinding) : RecyclerView.ViewHolder(listDropLocationBinding.root) {
        val dropLocationBinding = listDropLocationBinding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DropLocationViewHolder {
        val viewHolderBinding = DataBindingUtil.inflate<ListDeliveryDropLocationBinding>(LayoutInflater.from(parent.context), R.layout.list_delivery_drop_location, parent, false)
        return DropLocationViewHolder(viewHolderBinding)
    }

    override fun getItemCount(): Int {
        return dropLocationList.size
    }

    override fun onBindViewHolder(holder: DropLocationViewHolder, position: Int) {
            holder.dropLocationBinding.tvDropLocation.text = dropLocationList.get(position).d_address
    }
}