package com.xjek.provider.views.setup_vehicle

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.xjek.provider.BR
import com.xjek.provider.R
import com.xjek.provider.databinding.LayoutSetupVehicleItemBinding

class SetupVehicleAdapter(private val setupVehicleViewModel: SetupVehicleViewModel) :
        RecyclerView.Adapter<SetupVehicleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<LayoutSetupVehicleItemBinding>(
                layoutInflater,
                R.layout.layout_setup_vehicle_item,
                parent,
                false)
        return SetupVehicleAdapter.ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return setupVehicleViewModel.getItemCount()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(setupVehicleViewModel, position)
    }

    class ViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(setupVehicleViewModel: SetupVehicleViewModel, position: Int) {
            binding.setVariable(BR.setupVehicleViewModel, setupVehicleViewModel)
            binding.setVariable(BR.position, position)
            binding.executePendingBindings()
        }
    }
}