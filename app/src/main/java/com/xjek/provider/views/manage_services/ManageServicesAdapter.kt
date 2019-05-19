package com.xjek.provider.views.manage_services

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.xjek.provider.BR
import com.xjek.provider.R
import com.xjek.provider.databinding.LayoutManageServicesItemBinding

class ManageServicesAdapter(private val manageServicesViewModel: ManageServicesViewModel) :
        RecyclerView.Adapter<ManageServicesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<LayoutManageServicesItemBinding>(
                layoutInflater,
                R.layout.layout_manage_services_item,
                parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return manageServicesViewModel.getServiceData().size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(manageServicesViewModel, position)
    }

    class ViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(manageServicesViewModel: ManageServicesViewModel, position: Int) {
            binding.setVariable(BR.manageServicesViewModel, manageServicesViewModel)
            binding.setVariable(BR.position, position)
            binding.executePendingBindings()
        }
    }
}