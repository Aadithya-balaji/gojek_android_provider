package com.xjek.provider.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.xjek.provider.R
import com.xjek.provider.databinding.CountrySearchListItemBinding
import com.xjek.provider.databinding.RowPaymentModeBinding
import com.xjek.provider.interfaces.CustomClickListner
import kotlinx.android.synthetic.main.row_payment_mode.view.*

class  PaymentModeAdapter(context: Context,paymentList:MutableList<String> ):RecyclerView.Adapter<PaymentModeAdapter.PaymentViewHolder>(){

    private  var context:Context?=null
    private  var paymentList:MutableList<String>?=null

    init {
        this.context=context
        this.paymentList=paymentList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val inflate = DataBindingUtil.inflate<RowPaymentModeBinding>(LayoutInflater.from(parent.context), R.layout.row_payment_mode, parent, false)
        return PaymentViewHolder(inflate)    }

    override fun getItemCount(): Int {
       return paymentList!!.size
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        //holder.RowtvPaymentMode.setText(paymentList?.let { paymentList!!.get(position) })
        holder.paymentBinding.tvPaymentMode.setText(paymentList!!.get(position))
        holder.paymentBinding.paymentmodel=object:CustomClickListner{
            override fun onListClickListner() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
    }
    inner class  PaymentViewHolder(rowItemBinding:RowPaymentModeBinding):RecyclerView.ViewHolder(rowItemBinding.root){
        val paymentBinding=rowItemBinding
    }
}