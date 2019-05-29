package com.gox.partner.views.setup_services

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.gox.partner.BR
import com.gox.partner.R
import com.gox.partner.databinding.LayoutManageServicesItemBinding

class SetupServicesAdapter(private val setupServicesViewModel: SetupServicesViewModel) :
        RecyclerView.Adapter<SetupServicesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<LayoutManageServicesItemBinding>(
                layoutInflater,
                R.layout.layout_setup_services_item,
                parent,
                false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return setupServicesViewModel.getItemCount()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(setupServicesViewModel, position)
    }

    class ViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(setupServicesViewModel: SetupServicesViewModel, position: Int) {
//            binding.setVariable(BR.setupSer, setupServicesViewModel)
            binding.setVariable(BR.position, position)
            binding.executePendingBindings()
        }
    }
}