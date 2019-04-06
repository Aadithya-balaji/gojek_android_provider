package com.xjek.provider.views.manage_services

import com.xjek.base.base.BaseViewModel
import com.xjek.provider.models.ManageServicesModel

class ManageServicesViewModel : BaseViewModel<ManageServicesNavigator>() {

    private lateinit var adapter: ManageServicesAdapter
    private lateinit var services: List<ManageServicesModel>

    fun setServices(services: List<ManageServicesModel>) {
        this.services = services
    }

    fun getServices(): List<ManageServicesModel> {
        return services
    }

    fun setAdapter() {
        adapter = ManageServicesAdapter(this)
        adapter.notifyDataSetChanged()
    }

    fun getAdapter(): ManageServicesAdapter {
        return adapter
    }

    fun getService(position: Int): ManageServicesModel {
        return services[position]
    }

    fun onItemClick(position: Int) {
        navigator.onMenuItemClicked(position)
    }
}