package com.gox.partner.views.set_service

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.gox.partner.R
import com.gox.partner.databinding.LayoutSetServicesItemBinding
import com.gox.partner.models.ServiceCategoriesResponse
import com.gox.partner.models.ServiceCategoriesResponse.ResponseData

class ServiceAdapter(val activity: SetServiceActivity, var subServiceData: ServiceCategoriesResponse)
    : RecyclerView.Adapter<ServiceAdapter.MyViewHolder>() {

    var serviceItemClick: ServiceItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context)
                , R.layout.layout_set_services_item, parent, false)
        )
    }

    interface ServiceItemClick {
        fun onItemClick(service: ResponseData)
    }

    override fun getItemCount() = subServiceData.responseData.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()
        holder.mBinding.serviceNameTv.text = subServiceData.responseData[position].service_category_name
        if (subServiceData.responseData[position].providerservicecategory.isNotEmpty())
            holder.mBinding.ivSelection.visibility = VISIBLE
        else
            holder.mBinding.ivSelection.visibility = GONE
        holder.mBinding.serviceCard.setOnClickListener {
            serviceItemClick?.onItemClick(subServiceData.responseData[position])
        }
        if (position == subServiceData.responseData.size - 1)
            holder.mBinding.view.visibility = GONE
        else holder.mBinding.view.visibility = VISIBLE
    }

    inner class MyViewHolder(itemView: LayoutSetServicesItemBinding)
        : RecyclerView.ViewHolder(itemView.root) {
        val mBinding = itemView
        fun bind() {

        }
    }
}
