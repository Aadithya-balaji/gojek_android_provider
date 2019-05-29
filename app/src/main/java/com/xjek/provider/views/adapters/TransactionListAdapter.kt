package com.xjek.provider.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.xjek.provider.R
import com.xjek.provider.databinding.TransactionListItemBinding
import com.xjek.provider.models.TransactionDatum
import com.xjek.provider.utils.CommanMethods

class TransactionListAdapter(context: Context, transactionList: List<TransactionDatum>) : RecyclerView.Adapter<TransactionListAdapter.MyViewHolder>() {

    var context: Context? = null
    var transactionList: List<TransactionDatum>? = null

    init {
        this.context = context
        this.transactionList = transactionList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val inflate = DataBindingUtil.inflate<TransactionListItemBinding>(LayoutInflater.from(parent.context), R.layout.transaction_list_item, parent, false)
        return MyViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return transactionList!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.currentOderItemlistBinding.tvTransactionID.setText(transactionList!!.get(position).getTransactionAlias().toString())
        var strDate = CommanMethods.getLocalTimeStamp(transactionList!!.get(position).getCreatedAt().toString())
        holder.currentOderItemlistBinding.tvTransactionAmount.setText(String.format(context!!.getString(R.string.transaction_amount), transactionList!!.get(position).getAmount()))
        if(transactionList!!.get(position).getType().equals("D"))
        {
           holder.currentOderItemlistBinding.tvTransactionStatus.setText(context!!.resources.getString(R.string.depited))
            holder.currentOderItemlistBinding.tvTransactionStatus.setTextColor(ContextCompat.getColor(context!!,R.color.dispute_status_open))
        }else{
            holder.currentOderItemlistBinding.tvTransactionStatus.setText(context!!.resources.getString(R.string.credited))
            holder.currentOderItemlistBinding.tvTransactionStatus.setTextColor(ContextCompat.getColor(context!!,R.color.credit))
        }
    }

    inner class MyViewHolder(itemView: TransactionListItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        val currentOderItemlistBinding = itemView
    }

}