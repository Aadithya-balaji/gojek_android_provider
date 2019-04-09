package com.xjek.provider.views.setup_vehicle

import com.xjek.base.base.BaseViewModel

class SetupVehicleViewModel : BaseViewModel<SetupVehicleNavigator>() {

    private lateinit var adapter: SetupVehicleAdapter
//    private lateinit var services: List<ManageServicesModel>

//    fun setServices(services: List<ManageServicesModel>) {
//        this.services = services
//    }

//    fun getServices(): List<ManageServicesModel> {
//        return services
//    }

    fun setAdapter() {
        adapter = SetupVehicleAdapter(this)
        adapter.notifyDataSetChanged()
    }

    fun getAdapter(): SetupVehicleAdapter {
        return adapter
    }

//    fun getService(position: Int): ManageServicesModel {
//        return services[position]
//    }

    fun onItemClick(position: Int) {
        navigator.onMenuItemClicked(position)
    }
}