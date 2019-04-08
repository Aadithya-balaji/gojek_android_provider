package com.xjek.provider.views.manage_services

import com.xjek.base.base.BaseViewModel
import com.xjek.provider.models.ManageServicesDataModel

class ManageServicesViewModel : BaseViewModel<ManageServicesNavigator>() {

    private lateinit var adapter: ManageServicesAdapter
    private lateinit var services: List<ManageServicesDataModel>

    fun setServices(services: List<ManageServicesDataModel>) {
        this.services = services
    }

    fun getServices(): List<ManageServicesDataModel> {
        return services
    }

    fun setAdapter() {
        adapter = ManageServicesAdapter(this)
        adapter.notifyDataSetChanged()
    }

    fun getAdapter(): ManageServicesAdapter {
        return adapter
    }

    fun getService(position: Int): ManageServicesDataModel {
        return services[position]
    }

    fun onItemClick(position: Int) {
        navigator.onMenuItemClicked(position)
    }
}