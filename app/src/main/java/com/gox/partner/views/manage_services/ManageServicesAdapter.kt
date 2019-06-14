package com.gox.partner.views.manage_services

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.gox.partner.BR
import com.gox.partner.R
import com.gox.partner.databinding.LayoutManageServicesItemBinding

class ManageServicesAdapter(private val manageServicesViewModel: ManageServicesViewModel)
    : RecyclerView.Adapter<ManageServicesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<LayoutManageServicesItemBinding>(
                layoutInflater,
                R.layout.layout_manage_services_item,
                parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = manageServicesViewModel.getServiceData().size

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