package com.gox.partner.views.setup_vehicle

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.gox.partner.BR
import com.gox.partner.R
import com.gox.partner.databinding.LayoutSetupVehicleItemBinding

class SetupVehicleAdapter(private val setupVehicleViewModel: SetupVehicleViewModel) :
        RecyclerView.Adapter<SetupVehicleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<LayoutSetupVehicleItemBinding>(
                layoutInflater, R.layout.layout_setup_vehicle_item,
                parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = setupVehicleViewModel.getItemCount()

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