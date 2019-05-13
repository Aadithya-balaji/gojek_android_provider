package com.xjek.provider.views.set_service_category_price

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.provider.R
import com.xjek.provider.databinding.LayoutSetServicesWithPriceItemBinding
import com.xjek.provider.models.SubServicePriceCategoriesResponse
import com.xjek.provider.models.SubServicePriceCategoriesResponse.ResponseData
import com.xjek.provider.utils.CommanMethods

class SubServicePriceAdapter(val activity: SetServicePriceActivity, var subserviceData: SubServicePriceCategoriesResponse, val priceEdit: Boolean)
    : RecyclerView.Adapter<SubServicePriceAdapter.MyViewHolder>() {

    var serviceItemClick: ServiceItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflate = DataBindingUtil.inflate<LayoutSetServicesWithPriceItemBinding>(LayoutInflater.from(parent.context)
                , R.layout.layout_set_services_with_price_item, parent, false)
        return MyViewHolder(inflate)
    }

    interface ServiceItemClick {
        fun onItemClick(service: ResponseData, isPriceEdit: Boolean)
    }

    override fun getItemCount(): Int = subserviceData.responseData.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()
        val currency = activity.readPreferences<String>(PreferencesKey.CURRENCY_SYMBOL)
        holder.subserviceRowlistItemBinding.serviceNameTv.text = subserviceData.responseData[position].service_name
        holder.subserviceRowlistItemBinding.priceEditImg.setOnClickListener {
            serviceItemClick?.onItemClick(subserviceData.responseData[position], true)
        }
        holder.subserviceRowlistItemBinding.serviceCard.setOnClickListener {
            serviceItemClick?.onItemClick(subserviceData.responseData[position], false)
        }

        when (subserviceData.responseData[position].selected == "1" || subserviceData.responseData[position].providerservices.isNotEmpty()) {
            true -> holder.subserviceRowlistItemBinding.selectImg.setImageResource(R.drawable.ic_check_box_selected)
            false -> holder.subserviceRowlistItemBinding.selectImg.setImageResource(R.drawable.ic_check_box)
        }

        when {
            priceEdit -> {
                holder.subserviceRowlistItemBinding.priceEditImg.visibility = VISIBLE
                holder.subserviceRowlistItemBinding.priceTv.visibility = VISIBLE
                var price = activity.getString(R.string.rate) + " " + currency
                if (subserviceData.responseData[position].servicescityprice != null &&
                        subserviceData.responseData[position].servicescityprice.base_fare != null) {
                    when (subserviceData.responseData[position].servicescityprice.fare_type) {
                        "DISTANCETIME" -> {
                            holder.subserviceRowlistItemBinding.perMilePriceTv.visibility = VISIBLE
                            holder.subserviceRowlistItemBinding.perMilePriceTv.text = price +
                                    subserviceData.responseData[position].servicescityprice.per_miles + " " +
                                    activity.getString(R.string.per_miles)
                        }
                        else -> holder.subserviceRowlistItemBinding.perMilePriceTv.visibility = GONE
                    }
                    price += CommanMethods.getFare(activity, subserviceData.responseData[position].servicescityprice)
                } else {
                    price += "0.0"
                    holder.subserviceRowlistItemBinding.perMilePriceTv.visibility = GONE
                }
                holder.subserviceRowlistItemBinding.priceTv.text = price
            }
            else -> {
                holder.subserviceRowlistItemBinding.priceTv.visibility = GONE
                holder.subserviceRowlistItemBinding.priceEditImg.visibility = GONE
                holder.subserviceRowlistItemBinding.perMilePriceTv.visibility = GONE
            }
        }
    }

    inner class MyViewHolder(itemView: LayoutSetServicesWithPriceItemBinding)
        : RecyclerView.ViewHolder(itemView.root) {
        val subserviceRowlistItemBinding = itemView
        fun bind() {

        }
    }
}
