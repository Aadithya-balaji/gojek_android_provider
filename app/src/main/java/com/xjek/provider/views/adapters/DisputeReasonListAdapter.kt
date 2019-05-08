package com.xjek.provider.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.xjek.provider.R
import com.xjek.provider.model.DisputeListData
import com.xjek.provider.views.history_details.HistoryDetailViewModel


class DisputeReasonListAdapter(val historyDetailViewModel: HistoryDetailViewModel
                               , val disputereasonList: List<DisputeListData>)
    : RecyclerView.Adapter<DisputeReasonListAdapter.MyViewHolder>() {

    private var mOnAdapterClickListener: ReasonListClicklistner? = null
    fun setOnClickListener(onClickListener: ReasonListClicklistner) {
        this.mOnAdapterClickListener = onClickListener
    }

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        this.context = parent.context
        val inflate = DataBindingUtil
                .inflate<FilterserviceRowitemBinding>(LayoutInflater.from(parent.context),
                        R.layout.filterservice_rowitem, parent, false)
        return MyViewHolder(inflate)
    }

    override fun getItemCount(): Int = disputereasonList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        holder.filterserviceRowitemBinding.filterserviceNameTv.text = (disputereasonList.get(position)
                .dispute_name).toLowerCase().capitalize()
        /* holder.filterserviceRowitemBinding.itemClickListener = object : CustomClickListener {
             override fun onListClickListener() {

                 Log.d("_D_ADP", disputereasonList[position].dispute_name + "")
                 // historyDetailViewModel.selectedDisputeName.value =
                 historyDetailViewModel.setSelectedValue(disputereasonList[position].dispute_name)

             }

         }*/

        holder.filterserviceRowitemBinding.filterserviceNameTv.setOnClickListener {
            if (mOnAdapterClickListener != null) {
                mOnAdapterClickListener!!.reasonOnItemClick((disputereasonList.get(position)
                        .dispute_name).toLowerCase().capitalize())
            }
        }


//
//        holder.bind()
    }


    inner class MyViewHolder(itemView: FilterserviceRowitemBinding) : RecyclerView.ViewHolder(itemView.root) {

        val filterserviceRowitemBinding = itemView

        fun bind() {

        }


    }


}


