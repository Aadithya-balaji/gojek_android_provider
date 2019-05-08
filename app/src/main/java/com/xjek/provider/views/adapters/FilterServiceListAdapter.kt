package com.xjek.provider.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.xjek.provider.R
import com.xjek.provider.interfaces.CustomClickListner
import com.xjek.provider.models.ConfigResponseModel
import com.xjek.provider.views.dashboard.DashBoardViewModel


class FilterServiceListAdapter(val orderFragmentViewModel: DashBoardViewModel, val filterServiceListName: List<ConfigResponseModel.ResponseData.Service>)
    : RecyclerView.Adapter<FilterServiceListAdapter.MyViewHolder>() {

    lateinit var context: Context
    private var selectedPosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        this.context = parent.context
        val inflate = DataBindingUtil
                .inflate<FilterserviceRowitemBinding>(LayoutInflater.from(parent.context),
                        R.layout.filterservice_rowitem, parent, false)
        return MyViewHolder(inflate)
    }

    override fun getItemCount(): Int = filterServiceListName.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (selectedPosition == position) {
            holder.filterserviceRowitemBinding.filterserviceNameTv.background =
                    ContextCompat.getDrawable(context
                            , R.drawable.custom_roundcorner_selectedorder)
        } else {
            holder.filterserviceRowitemBinding.filterserviceNameTv.background =
                    ContextCompat.getDrawable(context
                            , R.drawable.custom_roundcorner_unselectedorder)
        }

        holder.filterserviceRowitemBinding.filterserviceNameTv.text = (filterServiceListName.get(position)
                .adminServiceName).toLowerCase().capitalize()
        holder.filterserviceRowitemBinding.itemClickListener = object : CustomClickListner {
            override fun onListClickListner() {
                selectedPosition = holder.adapterPosition
                orderFragmentViewModel.selectedFilterService.value = filterServiceListName[holder.adapterPosition].adminServiceName
                holder.filterserviceRowitemBinding.filterserviceNameTv.background =
                        ContextCompat.getDrawable(context
                                , R.drawable.custom_roundcorner_selectedorder)
                notifyDataSetChanged()

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


