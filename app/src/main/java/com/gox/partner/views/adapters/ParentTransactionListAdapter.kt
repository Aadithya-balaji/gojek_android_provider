package com.gox.partner.views.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.gox.base.data.PlacesModel
import com.gox.partner.R
import com.gox.partner.databinding.ParentTransactionListItemBinding
import com.gox.partner.databinding.TransactionListItemBinding
import com.gox.partner.models.WalletTransaction
import com.gox.taxiservice.databinding.RowPlacesLayoutBinding

class ParentTransactionListAdapter(context: Context, transactionList: List<WalletTransaction.ResponseData.Data>)
    : RecyclerView.Adapter<ParentTransactionListAdapter.MyViewHolder>() {

    var context: Context? = null
    var transactionList: List<WalletTransaction.ResponseData.Data>? = null

    init {
        this.context = context
        this.transactionList = transactionList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.parent_transaction_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return transactionList!!.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mBinding.tvTransactionID.text = transactionList!![position].transaction_alias
        val transactionListAdapter = TransactionListAdapter(holder.itemView.context, transactionList!![position].transactions
        , transactionList!![position].provider.currency_symbol)
        holder.mBinding.transactionListRv.adapter = transactionListAdapter
        transactionListAdapter.notifyDataSetChanged()

    }

    inner class MyViewHolder(itemView: ParentTransactionListItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        val mBinding = itemView
    }


}