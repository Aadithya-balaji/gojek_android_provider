package com.xjek.provider.views.manage_documents

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.xjek.provider.BR
import com.xjek.provider.R
import com.xjek.provider.databinding.LayoutSetupVehicleItemBinding

class ManageDocumentsAdapter(private val manageDocumentsViewModel: ManageDocumentsViewModel) :
        RecyclerView.Adapter<ManageDocumentsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<LayoutSetupVehicleItemBinding>(
                layoutInflater,
                R.layout.layout_setup_vehicle_item,
                parent,
                false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(manageDocumentsViewModel, position)
    }

    class ViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(manageDocumentsViewModel: ManageDocumentsViewModel, position: Int) {
            binding.setVariable(BR.manageDocumentsViewModel, manageDocumentsViewModel)
            binding.setVariable(BR.position, position)
            binding.executePendingBindings()
        }
    }
}