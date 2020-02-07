package com.gox.partner.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.gox.partner.R
import com.gox.partner.databinding.RowDisputeListBinding
import com.gox.partner.models.DisputeListData
import com.gox.partner.views.history_details.HistoryDetailViewModel

class DisputeReasonListAdapter(val viewModel: HistoryDetailViewModel, private val disputeReason: List<DisputeListData>)
    : RecyclerView.Adapter<DisputeReasonListAdapter.MyViewHolder>() {

    private var mListener: ReasonListClickListener? = null
    private var selectedPosition = -1

    fun setOnClickListener(onClickListener: ReasonListClickListener) {
        this.mListener = onClickListener
    }

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        this.context = parent.context
        val inflate = DataBindingUtil.inflate<RowDisputeListBinding>(LayoutInflater.from(parent.context),
                R.layout.row_dispute_list, parent, false)
        return MyViewHolder(inflate)
    }

    override fun getItemCount(): Int = disputeReason.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.rowDisputeListBinding.llDisputeReaons.tag = position
        holder.rowDisputeListBinding.tvDisbuteReason.text = disputeReason[position].dispute_name

        holder.rowDisputeListBinding.rbDisbute.isChecked = selectedPosition == position
        holder.rowDisputeListBinding.rbDisbute.setOnCheckedChangeListener { _, _ ->
            onItemChecked(position)
        }
        holder.rowDisputeListBinding.llDisputeReaons.setOnClickListener {
            onItemChecked(position)
        }
    }

    private fun onItemChecked(index: Int) {
        val oldIndex = selectedPosition
        selectedPosition = index
        viewModel.selectedDisputeModel.value = disputeReason[selectedPosition]
        notifyItemChanged(oldIndex)
        notifyItemChanged(selectedPosition)
    }
    inner class MyViewHolder(itemView: RowDisputeListBinding) : RecyclerView.ViewHolder(itemView.root) {
        val rowDisputeListBinding = itemView
    }
}