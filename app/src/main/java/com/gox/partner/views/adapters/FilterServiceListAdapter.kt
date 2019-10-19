package com.gox.partner.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.gox.partner.R
import com.gox.partner.databinding.FilterserviceRowitemBinding
import com.gox.partner.interfaces.CustomClickListener
import com.gox.partner.interfaces.ServiceTypeListener
import com.gox.partner.models.ConfigService

class FilterServiceListAdapter(val filterServiceListName: List<ConfigService>,
                               private var serviceTypeListener: ServiceTypeListener) : RecyclerView.Adapter<FilterServiceListAdapter.MyViewHolder>() {

    lateinit var context: Context
    private var selectedPosition: Int = -1



    fun setSelectedPosition(selectedPosition:Int){
        this.selectedPosition = selectedPosition
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        this.context = parent.context
        return MyViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.filterservice_rowitem, parent, false))
    }

    override fun getItemCount(): Int = filterServiceListName.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (selectedPosition == position && selectedPosition != -1)
            holder.mBinding.filterserviceNameTv.background =
                    ContextCompat.getDrawable(context, R.drawable.custom_roundcorner_selectedorder)
        else holder.mBinding.filterserviceNameTv.background =
                ContextCompat.getDrawable(context, R.drawable.custom_roundcorner_unselectedorder)
        holder.mBinding.filterserviceNameTv.text =
                (filterServiceListName[position].admin_service).toLowerCase().capitalize()
        holder.mBinding.itemClickListener = object : CustomClickListener {
            override fun onListClickListener() {
                selectedPosition = holder.adapterPosition
                holder.mBinding.filterserviceNameTv.background =
                        ContextCompat.getDrawable(context, R.drawable.custom_roundcorner_selectedorder)
                serviceTypeListener.getServiceType(filterServiceListName[position].admin_service.toLowerCase(), filterServiceListName[position].id,position)
                notifyDataSetChanged()
            }
        }
    }

    inner class MyViewHolder(itemView: FilterserviceRowitemBinding) : RecyclerView.ViewHolder(itemView.root) {
        val mBinding = itemView
    }
}