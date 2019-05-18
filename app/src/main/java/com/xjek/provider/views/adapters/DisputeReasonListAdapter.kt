package com.xjek.provider.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.xjek.provider.R
import com.xjek.provider.databinding.RowDisputeListBinding
import com.xjek.provider.model.DisputeListData
import com.xjek.provider.views.history_details.HistoryDetailViewModel


class DisputeReasonListAdapter(val historyDetailViewModel: HistoryDetailViewModel, val disputereasonList: List<DisputeListData>) : RecyclerView.Adapter<DisputeReasonListAdapter.MyViewHolder>() {

    private var mOnAdapterClickListener: ReasonListClicklistner? = null
    private var selectedPosition = -1

    fun setOnClickListener(onClickListener: ReasonListClicklistner) {
        this.mOnAdapterClickListener = onClickListener
    }

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        this.context = parent.context
        val inflate = DataBindingUtil.inflate<RowDisputeListBinding>(LayoutInflater.from(parent.context),
                R.layout.row_dispute_list, parent, false)
        return MyViewHolder(inflate)
    }

    override fun getItemCount(): Int = disputereasonList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.rowDisputeListBinding.llDisputeReaons.tag = position
        holder.rowDisputeListBinding.tvDisbuteReason.setText(disputereasonList.get(position).dispute_name)

        if(selectedPosition==position){
            holder.rowDisputeListBinding.rbDisbute.isChecked=true
        }else{
            holder.rowDisputeListBinding.rbDisbute.isChecked=false


        }
        /*holder.rowDisputeListBinding.llDisputeReaons.setOnClickListener {
            if (mOnAdapterClickListener != null) {
                mOnAdapterClickListener!!.reasonOnItemClick((disputereasonList.get(position)
                        .dispute_name).toLowerCase().capitalize())
            }
        }*/
        holder.rowDisputeListBinding.llDisputeReaons.setOnClickListener { v -> itemCheckChanged(v) }
    }


    private fun itemCheckChanged(v: View) {
        selectedPosition = v.tag as Int
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: RowDisputeListBinding) : RecyclerView.ViewHolder(itemView.root) {
        val rowDisputeListBinding = itemView
    }


}


