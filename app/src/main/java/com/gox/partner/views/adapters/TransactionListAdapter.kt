package com.gox.partner.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.gox.base.data.PlacesModel
import com.gox.partner.R
import com.gox.partner.databinding.TransactionListItemBinding
import com.gox.partner.models.WalletTransaction
import com.gox.taxiservice.databinding.RowPlacesLayoutBinding

class TransactionListAdapter(context: Context, transactionList: List<WalletTransaction.ResponseData.Data.TransactionFormat>,var currency_symbol:String)
    : RecyclerView.Adapter<TransactionListAdapter.MyViewHolder>() {

    var context: Context? = null
    var transactionList: List<WalletTransaction.ResponseData.Data.TransactionFormat>? = null

    init {
        this.context = context
        this.transactionList = transactionList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.transaction_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return transactionList!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mBinding.tvTransactionDesc.text = transactionList!![position].transaction_desc
//        var strDate = CommanMethods.getLocalTimeStamp(transactionList!![position].created_at)
        holder.mBinding.tvTransactionAmount.text = currency_symbol+" "+String.format(context!!.getString(R.string.transaction_amount), transactionList!!.get(position).amount)
        if (transactionList!![position].type == "D") {
            holder.mBinding.tvTransactionStatus.text = context!!.resources.getString(R.string.depited)
            holder.mBinding.tvTransactionStatus.setTextColor(ContextCompat.getColor(context!!,
                    R.color.dispute_status_open))
        } else {
            holder.mBinding.tvTransactionStatus.text = context!!.resources.getString(R.string.credited)
            holder.mBinding.tvTransactionStatus.setTextColor(ContextCompat.getColor(context!!,
                    R.color.credit))
        }
    }

    inner class MyViewHolder(itemView: TransactionListItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        val mBinding = itemView
    }


}