package com.xjek.provider.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.xjek.provider.R
import com.xjek.provider.interfaces.CustomClickListner
import com.xjek.provider.models.ConfigResponseModel
import com.xjek.provider.views.wallet.WalletViewModel

class PaymentModeAdapter(context: Context, paymentList: MutableList<String>, payTypes: List<ConfigResponseModel.ResponseData.AppSetting.Payments>, walletViewModel: WalletViewModel) : RecyclerView.Adapter<PaymentModeAdapter.PaymentViewHolder>() {

    private var context: Context? = null
    private var paymentList: MutableList<String>? = null
    private var selectedPosition: Int? = -1
    private var payTypes: List<ConfigResponseModel.ResponseData.AppSetting.Payments>? = null
    private var walletViewModel: WalletViewModel? = null

    init {
        this.context = context
        this.paymentList = paymentList
        this.payTypes = payTypes
        this.walletViewModel = walletViewModel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val inflate = DataBindingUtil.inflate<RowPaymentModeBinding>(LayoutInflater.from(parent.context), R.layout.row_payment_mode, parent, false)
        return PaymentViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return payTypes!!.size
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        //holder.RowtvPaymentMode.setText(paymentList?.let { paymentList!!.get(position) })
        holder.paymentBinding.tvPaymentMode.setText(payTypes!!.get(position).name)
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
                holder.paymentBinding.tvPaymentMode.background = ContextCompat.getDrawable(context!!, R.drawable.bg_blue_rounded_corner)
                holder.paymentBinding.tvPaymentMode.setTextColor(ContextCompat.getColor(context!!, R.color.white))
                notifyDataSetChanged()
            }
        }
    }

    inner class PaymentViewHolder(rowItemBinding: RowPaymentModeBinding) : RecyclerView.ViewHolder(rowItemBinding.root) {
        val paymentBinding = rowItemBinding
    }
}