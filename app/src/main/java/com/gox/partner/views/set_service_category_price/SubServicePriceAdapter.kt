package com.gox.partner.views.set_service_category_price

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.gox.base.data.Constants.FareType.DISTANCE_TIME
import com.gox.base.data.Constants.FareType.FIXED
import com.gox.base.data.Constants.FareType.HOURLY
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.partner.R
import com.gox.partner.databinding.LayoutSetServicesWithPriceItemBinding
import com.gox.partner.models.SubServicePriceCategoriesResponse
import com.gox.partner.models.SubServicePriceResponseData

class SubServicePriceAdapter(val activity: SetServicePriceActivity, private var subServiceData: SubServicePriceCategoriesResponse,
                             private val priceEdit: Boolean) : RecyclerView.Adapter<SubServicePriceAdapter.MyViewHolder>() {

    var serviceItemClick: ServiceItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            MyViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                    R.layout.layout_set_services_with_price_item, parent, false))

    interface ServiceItemClick {
        fun onItemClick(service: SubServicePriceResponseData, isPriceEdit: Boolean)
    }

    override fun getItemCount() = subServiceData.responseData!!.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()
        val currency = activity.readPreferences<String>(PreferencesKey.CURRENCY_SYMBOL)
        holder.mBinding.serviceNameTv.text = subServiceData.responseData!![position]!!.service_name

        holder.mBinding.priceEditImg.setOnClickListener {
            serviceItemClick?.onItemClick(subServiceData.responseData!![position]!!, true)
        }

        holder.mBinding.serviceCard.setOnClickListener {
            serviceItemClick?.onItemClick(subServiceData.responseData!![position]!!, false)
        }

        when (subServiceData.responseData!![position]!!.selected == "1"
                || subServiceData.responseData!![position]!!.providerservices!!.isNotEmpty()) {
            true -> holder.mBinding.selectImg.setImageResource(R.drawable.ic_check_box_selected)
            false -> holder.mBinding.selectImg.setImageResource(R.drawable.ic_check_box)
        }

        when {
            priceEdit -> {
                holder.mBinding.priceEditImg.visibility = VISIBLE
                holder.mBinding.tvPrice.visibility = VISIBLE
                var price = activity.getString(R.string.rate) + " " + currency
                price += if (subServiceData.responseData!![position]!!.providerservices?.isNotEmpty()!!
                        && subServiceData.responseData!![position]!!.providerservices?.isNotEmpty()!!)
                    when (subServiceData.responseData!![position]!!.service_city?.fare_type) {
                        DISTANCE_TIME -> {
                            if (subServiceData.responseData!![position]!!.providerservices!![0]!!.per_mins!! > 0
                                    && subServiceData.responseData!![position]!!.providerservices!![0]!!.per_miles!! > 0)
                                subServiceData.responseData!![position]!!.providerservices!![0]!!.per_mins.toString() +
                                        " " + activity.getString(R.string.per_min) +
                                        " / " + subServiceData.responseData!![position]!!.providerservices!![0]!!.per_miles +
                                        activity.getString(R.string.per_miles)
                            else subServiceData.responseData!![position]!!.service_city!!.per_mins.toString() +
                                    " " + activity.getString(R.string.per_min) +
                                    " / " + subServiceData.responseData!![position]!!.service_city!!.per_miles +
                                    activity.getString(R.string.per_miles)
                        }
                        HOURLY -> {
                            if (subServiceData.responseData!![position]!!.providerservices!![0]!!.per_mins!! > 0)
                                subServiceData.responseData!![position]!!.providerservices!![0]!!.per_mins.toString() +
                                        " " + activity.getString(R.string.per_hour)
                            else subServiceData.responseData!![position]!!.service_city!!.per_mins.toString() +
                                    " " + activity.getString(R.string.per_hour)
                        }
                        FIXED -> {
                            if (subServiceData.responseData!![position]!!.providerservices!![0]!!.base_fare!! > 0)
                                subServiceData.responseData!![position]!!.providerservices!![0]!!.base_fare.toString()
                            else subServiceData.responseData!![position]!!.service_city!!.base_fare
                        }
                        else -> "0"
                    } else "0"
                holder.mBinding.tvPrice.text = price
            }
            else -> {
                holder.mBinding.tvPrice.visibility = GONE
                holder.mBinding.priceEditImg.visibility = GONE
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
