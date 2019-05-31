package com.gox.partner.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.gox.partner.R
import com.gox.partner.databinding.RowPaymentModeBinding
import com.gox.partner.interfaces.CustomClickListner
import com.gox.partner.models.ConfigResponseModel
import com.gox.partner.views.account_card.CardListViewModel

class PaymentModeAdapter(context: Context, payTypes: List<ConfigResponseModel.ResponseData.AppSetting.Payments>, cardListViewModel: CardListViewModel,isFromWallet:Boolean) : RecyclerView.Adapter<PaymentModeAdapter.PaymentViewHolder>() {

    private var context: Context? = null
    private var paymentList: MutableList<String>? = null
    private var selectedPosition: Int? = -1
    private var payTypes: List<ConfigResponseModel.ResponseData.AppSetting.Payments>? = null
    private var cardListViewModel: CardListViewModel? = null
    private  var isFromWallet:Boolean?=false

    init {
        this.context = context
        this.payTypes = payTypes
        this.cardListViewModel = cardListViewModel
        this.isFromWallet=isFromWallet
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
            if(isFromWallet!!)
            holder.paymentBinding.tvPaymentMode.visibility = View.GONE
            else
                holder.paymentBinding.tvPaymentMode.visibility = View.VISIBLE

            holder.paymentBinding.tvPaymentMode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_payment_card,0,0,0)
        } else  {
            holder.paymentBinding.tvPaymentMode.visibility = View.GONE
            holder.paymentBinding.tvPaymentMode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_payment_cash,0,0,0)
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