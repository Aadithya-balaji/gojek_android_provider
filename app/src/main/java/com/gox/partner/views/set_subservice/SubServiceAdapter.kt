package com.gox.partner.views.set_subservice

import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.gox.partner.R
import com.gox.partner.databinding.LayoutSetServicesItemBinding
import com.gox.partner.models.SubServiceCategoriesResponse
import com.gox.partner.models.SubServiceCategoriesResponse.ResponseData

class SubServiceAdapter(val activity: SetSubServiceActivity, var subserviceData: SubServiceCategoriesResponse)
    : RecyclerView.Adapter<SubServiceAdapter.MyViewHolder>() {

    var serviceItemClick: ServiceItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context)
                , R.layout.layout_set_services_item, parent, false))
    }

    interface ServiceItemClick {
        fun onItemClick(service: ResponseData)
    }

    override fun getItemCount() = subserviceData.responseData.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()
        holder.mBinding.serviceNameTv.text = subserviceData.responseData[position].service_subcategory_name
        if (subserviceData.responseData[position].providerservicesubcategory.isNotEmpty())
            holder.mBinding.ivSelection.visibility = VISIBLE
        else
            holder.mBinding.ivSelection.visibility = View.GONE
        holder.mBinding.serviceCard.setOnClickListener {
            serviceItemClick?.onItemClick(subserviceData.responseData[position])
        }
        if (position == subserviceData.responseData.size - 1)
            holder.mBinding.view.visibility = View.GONE
        else
            holder.mBinding.view.visibility = VISIBLE
    }

    inner class MyViewHolder(itemView: LayoutSetServicesItemBinding)
        : RecyclerView.ViewHolder(itemView.root) {
        val mBinding = itemView
        fun bind() {

        }
    }
}
