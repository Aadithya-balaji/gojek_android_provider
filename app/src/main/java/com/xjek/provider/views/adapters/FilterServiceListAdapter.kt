package com.xjek.provider.views.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.xjek.provider.R
import com.xjek.provider.databinding.FilterserviceRowitemBinding
import com.xjek.provider.interfaces.CustomClickListner
import com.xjek.provider.interfaces.ServiceTypeListener
import com.xjek.provider.models.ConfigResponseModel
import com.xjek.provider.views.dashboard.DashBoardViewModel


class FilterServiceListAdapter(val orderFragmentViewModel: DashBoardViewModel, val filterServiceListName: List<ConfigResponseModel.ResponseData.Service>, serviceTypeListener: ServiceTypeListener, selectedService: Int) : RecyclerView.Adapter<FilterServiceListAdapter.MyViewHolder>() {

    lateinit var context: Context
    private var selectedPosition: Int = -1
    private lateinit var serviceTypeListener: ServiceTypeListener
    private var selectedServiceType: Int? = -1

    init {
        this.serviceTypeListener = serviceTypeListener
        this.selectedServiceType = selectedService
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        this.context = parent.context
        val inflate = DataBindingUtil.inflate<FilterserviceRowitemBinding>(LayoutInflater.from(parent.context), R.layout.filterservice_rowitem, parent, false)
        return MyViewHolder(inflate)
    }

    override fun getItemCount(): Int = filterServiceListName.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (selectedPosition == position && selectedPosition!=-1) {
            holder.filterserviceRowitemBinding.filterserviceNameTv.background =
                    ContextCompat.getDrawable(context, R.drawable.custom_roundcorner_selectedorder)
        } else if (filterServiceListName.get(position).id == selectedServiceType) {
            holder.filterserviceRowitemBinding.filterserviceNameTv.background =
                    ContextCompat.getDrawable(context, R.drawable.custom_roundcorner_selectedorder)
        } else {
            holder.filterserviceRowitemBinding.filterserviceNameTv.background = ContextCompat.getDrawable(context, R.drawable.custom_roundcorner_unselectedorder)

        }
        holder.filterserviceRowitemBinding.filterserviceNameTv.text = (filterServiceListName.get(position).adminServiceName).toLowerCase().capitalize()
        holder.filterserviceRowitemBinding.itemClickListener = object : CustomClickListner {
            override fun onListClickListner() {
                selectedPosition = holder.adapterPosition
                selectedServiceType=-1
                holder.filterserviceRowitemBinding.filterserviceNameTv.background = ContextCompat.getDrawable(context, R.drawable.custom_roundcorner_selectedorder)
                serviceTypeListener.getServiceType(filterServiceListName.get(position).adminServiceName.toLowerCase(), filterServiceListName.get(position).id)
                notifyDataSetChanged()

            }

        }

    }


    inner class MyViewHolder(itemView: FilterserviceRowitemBinding) : RecyclerView.ViewHolder(itemView.root) {

        val filterserviceRowitemBinding = itemView


    }

}


