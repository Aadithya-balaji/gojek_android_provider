package com.xjek.provider.views.set_service

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.xjek.provider.R
import com.xjek.provider.databinding.LayoutSetServicesItemBinding
import com.xjek.provider.models.ServiceCategoriesResponse
import com.xjek.provider.models.ServiceCategoriesResponse.ResponseData

class ServiceAdapter(val activity: SetServiceActivity, var subserviceData: ServiceCategoriesResponse)
    : RecyclerView.Adapter<ServiceAdapter.MyViewHolder>() {

    var serviceItemClick: ServiceItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflate = DataBindingUtil.inflate<LayoutSetServicesItemBinding>(LayoutInflater.from(parent.context)
                , R.layout.layout_set_services_item, parent, false)
        return MyViewHolder(inflate)
    }

    interface ServiceItemClick {
        fun onItemClick(service: ResponseData)
    }

    override fun getItemCount(): Int = subserviceData.responseData.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()
        holder.subserviceRowlistItemBinding.serviceNameTv.text = subserviceData.responseData[position].service_category_name
        holder.subserviceRowlistItemBinding.serviceCard.setOnClickListener {
            serviceItemClick?.onItemClick(subserviceData.responseData[position])
        }
    }

    inner class MyViewHolder(itemView: LayoutSetServicesItemBinding)
        : RecyclerView.ViewHolder(itemView.root) {
        val subserviceRowlistItemBinding = itemView
        fun bind() {

        }
    }
}
