package com.xjek.provider.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.xjek.provider.R
import com.xjek.provider.databinding.TransactionListItemBinding
import com.xjek.provider.models.TransactionDatum
import com.xjek.provider.models.TranscationModel
import com.xjek.provider.utils.CommanMethods

class TransactionListAdapter(context: Context, transactionList:List<TransactionDatum>) : RecyclerView.Adapter<TransactionListAdapter.MyViewHolder>() {

    var  context:Context?=null
    var   transactionList:List<TransactionDatum>?=null

    init {
        this.context=context
        this.transactionList=transactionList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val inflate = DataBindingUtil.inflate<TransactionListItemBinding>(LayoutInflater.from(parent.context), R.layout.transaction_list_item, parent, false)
        return MyViewHolder(inflate)
    }

    override fun getItemCount():Int{
        return transactionList!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       holder.currentOderItemlistBinding.transactionIdTv.setText(transactionList!!.get(position).getTransactionId().toString())
        var strDate=CommanMethods.getLocalTimeStamp(transactionList!!.get(position).getPaymentLog()!!.getCreatedAt().toString())
        holder.currentOderItemlistBinding.transactionDateTv.setText(strDate)
        holder.currentOderItemlistBinding.transactionAmountTv.setText(String.format(context!!.getString(R.string.transaction_amount),transactionList!!.get(position).getAmount()))
    }

    inner class MyViewHolder(itemView: TransactionListItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        val currentOderItemlistBinding = itemView
    }

}