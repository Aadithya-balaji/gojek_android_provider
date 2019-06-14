package com.gox.partner.views.set_service_category_price

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.gox.base.data.Constants.FareType.DISTANCE_TIME
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.partner.R
import com.gox.partner.databinding.LayoutSetServicesWithPriceItemBinding
import com.gox.partner.models.SubServicePriceCategoriesResponse
import com.gox.partner.models.SubServicePriceCategoriesResponse.ResponseData
import com.gox.partner.utils.CommonMethods

class SubServicePriceAdapter(val activity: SetServicePriceActivity, var subServiceData: SubServicePriceCategoriesResponse,
                             private val priceEdit: Boolean) : RecyclerView.Adapter<SubServicePriceAdapter.MyViewHolder>() {

    var serviceItemClick: ServiceItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.layout_set_services_with_price_item, parent, false)
        )
    }

    interface ServiceItemClick {
        fun onItemClick(service: ResponseData, isPriceEdit: Boolean)
    }

    override fun getItemCount(): Int = subServiceData.responseData.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()
        val currency = activity.readPreferences<String>(PreferencesKey.CURRENCY_SYMBOL)
        holder.mBinding.serviceNameTv.text = subServiceData.responseData[position].service_name
        holder.mBinding.priceEditImg.setOnClickListener {
            serviceItemClick?.onItemClick(subServiceData.responseData[position], true)
        }
        holder.mBinding.serviceCard.setOnClickListener {
            serviceItemClick?.onItemClick(subServiceData.responseData[position], false)
        }

        when (subServiceData.responseData[position].selected == "1" || subServiceData.responseData[position].providerservices.isNotEmpty()) {
            true -> holder.mBinding.selectImg.setImageResource(R.drawable.ic_check_box_selected)
            false -> holder.mBinding.selectImg.setImageResource(R.drawable.ic_check_box)
        }

        when {
            priceEdit -> {
                holder.mBinding.priceEditImg.visibility = VISIBLE
                holder.mBinding.priceTv.visibility = VISIBLE
                var price = activity.getString(R.string.rate) + " " + currency
                if (subServiceData.responseData[position].servicescityprice != null &&
                        subServiceData.responseData[position].servicescityprice.base_fare != null) {
                    when (subServiceData.responseData[position].servicescityprice.fare_type) {
                        DISTANCE_TIME -> {
                            holder.mBinding.perMilePriceTv.visibility = VISIBLE
                            if (subServiceData.responseData[position].providerservices.isNotEmpty())
                                holder.mBinding.perMilePriceTv.text = price +
                                        subServiceData.responseData[position].providerservices[0].per_miles + " " +
                                        activity.getString(R.string.per_miles)
                            else
                                holder.mBinding.perMilePriceTv.text = price +
                                        subServiceData.responseData[position].servicescityprice.per_miles + " " +
                                        activity.getString(R.string.per_miles)
                        }
                        else -> holder.mBinding.perMilePriceTv.visibility = GONE
                    }

                    price += CommonMethods.getFare(activity, subServiceData.responseData[position].servicescityprice, subServiceData.responseData[position].providerservices)
                } else {
                    if (subServiceData.responseData[position].providerservices.isNotEmpty())
                        price += subServiceData.responseData[position].providerservices[0].base_fare
                    else
                        price += "0.0"
                    holder.mBinding.perMilePriceTv.visibility = GONE
                }
                holder.mBinding.priceTv.text = price
            }
            else -> {
                holder.mBinding.priceTv.visibility = GONE
                holder.mBinding.priceEditImg.visibility = GONE
                holder.mBinding.perMilePriceTv.visibility = GONE
            }
        }
    }

    inner class MyViewHolder(itemView: LayoutSetServicesWithPriceItemBinding)
        : RecyclerView.ViewHolder(itemView.root) {
        val mBinding = itemView
        fun bind() {

        }
    }
}
