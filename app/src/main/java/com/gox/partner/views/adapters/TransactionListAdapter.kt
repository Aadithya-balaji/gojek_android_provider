package com.gox.partner.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.gox.partner.R
import com.gox.partner.databinding.TransactionListItemBinding
import com.gox.partner.models.WalletTransaction
import com.gox.partner.utils.CommonMethods

class TransactionListAdapter(context: Context, transactionList: List<WalletTransaction.ResponseData.Data>) : RecyclerView.Adapter<TransactionListAdapter.MyViewHolder>() {

    var context: Context? = null
    var transactionList: List<WalletTransaction.ResponseData.Data>? = null

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
//<<<<<<< HEAD
//        holder.currentOderItemlistBinding.tvTransactionID.setText(transactionList!![position].getTransactionAlias().toString())
//        var strDate = CommonMethods.getLocalTimeStamp(transactionList!![position].getCreatedAt().toString())
//        holder.currentOderItemlistBinding.tvTransactionAmount.text = String.format(context!!.getString(R.string.transaction_amount), transactionList!!.get(position).getAmount())
//        if(transactionList!!.get(position).getType().equals("D"))
//=======
        holder.currentOderItemlistBinding.tvTransactionID.text = transactionList!![position].transaction_alias
        var strDate = CommanMethods.getLocalTimeStamp(transactionList!![position].created_at)
        holder.currentOderItemlistBinding.tvTransactionAmount.text = String.format(context!!.getString(R.string.transaction_amount), transactionList!!.get(position).amount)
        if(transactionList!![position].type == "D")
        {
           holder.currentOderItemlistBinding.tvTransactionStatus.text = context!!.resources.getString(R.string.depited)
            holder.currentOderItemlistBinding.tvTransactionStatus.setTextColor(ContextCompat.getColor(context!!,R.color.dispute_status_open))
        }else{
            holder.currentOderItemlistBinding.tvTransactionStatus.text = context!!.resources.getString(R.string.credited)
            holder.currentOderItemlistBinding.tvTransactionStatus.setTextColor(ContextCompat.getColor(context!!,R.color.credit))
        }
    }

    inner class MyViewHolder(itemView: TransactionListItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        val currentOderItemlistBinding = itemView
    }

}