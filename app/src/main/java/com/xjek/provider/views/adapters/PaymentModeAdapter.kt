package com.xjek.provider.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.xjek.provider.R
import com.xjek.provider.databinding.RowPaymentModeBinding
import com.xjek.provider.interfaces.CustomClickListner
import com.xjek.provider.models.ConfigResponseModel
import com.xjek.provider.views.account_card.CardListViewModel

class PaymentModeAdapter(context: Context, payTypes: List<ConfigResponseModel.ResponseData.AppSetting.Payments>, cardListViewModel: CardListViewModel) : RecyclerView.Adapter<PaymentModeAdapter.PaymentViewHolder>() {

    private var context: Context? = null
    private var paymentList: MutableList<String>? = null
    private var selectedPosition: Int? = -1
    private var payTypes: List<ConfigResponseModel.ResponseData.AppSetting.Payments>? = null
    private var cardListViewModel: CardListViewModel? = null

    init {
        this.context = context
        this.payTypes = payTypes
        this.cardListViewModel = cardListViewModel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val inflate = DataBindingUtil.inflate<RowPaymentModeBinding>(LayoutInflater.from(parent.context), R.layout.row_payment_mode, parent, false)
        return PaymentViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return payTypes!!.size
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        holder.paymentBinding.tvPaymentMode.setText(payTypes!!.get(position).name)
        if (payTypes!!.get(position).name.equals("card")) {
            holder.paymentBinding.tvPaymentMode.visibility = View.VISIBLE
        } else {
            holder.paymentBinding.tvPaymentMode.visibility = View.GONE
        }
        if (selectedPosition == position) {
            holder.paymentBinding.tvPaymentMode.background = ContextCompat.getDrawable(context!!, R.drawable.bg_blue_rounded_corner)
            holder.paymentBinding.tvPaymentMode.setTextColor(ContextCompat.getColor(context!!, R.color.white))
        } else {
            holder.paymentBinding.tvPaymentMode.background = ContextCompat.getDrawable(context!!, R.drawable.bg_white)
            holder.paymentBinding.tvPaymentMode.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        }
        holder.paymentBinding.paymentmodel = object : CustomClickListner {
            override fun onListClickListner() {
                selectedPosition = holder.adapterPosition
               // holder.paymentBinding.tvPaymentMode.background = ContextCompat.getDrawable(context!!, R.drawable.bg_blue_rounded_corner)
               // holder.paymentBinding.tvPaymentMode.setTextColor(ContextCompat.getColor(context!!, R.color.white))
               // notifyDataSetChanged()
            }
        }
    }

    inner class PaymentViewHolder(rowItemBinding: RowPaymentModeBinding) : RecyclerView.ViewHolder(rowItemBinding.root) {
        val paymentBinding = rowItemBinding
    }
}