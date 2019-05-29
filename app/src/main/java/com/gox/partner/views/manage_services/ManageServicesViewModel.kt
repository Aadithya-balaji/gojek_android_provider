package com.gox.partner.views.manage_services

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.partner.models.ManageServicesDataModel
import com.gox.partner.models.ManageServicesResponseModel
import com.gox.partner.repository.AppRepository

class ManageServicesViewModel : BaseViewModel<ManageServicesNavigator>() {

    private val appRepository = AppRepository.instance()
    private val servicesLiveData = MutableLiveData<ManageServicesResponseModel>()

    private var serviceData: List<ManageServicesDataModel> = emptyList()
    private val adapter: ManageServicesAdapter = ManageServicesAdapter(this)

    fun setServiceData(serviceData: List<ManageServicesDataModel>) {
        this.serviceData = serviceData
    }

    fun getServiceData(): List<ManageServicesDataModel> {
        return serviceData
    }

    fun setAdapter() {
        adapter.notifyDataSetChanged()
    }

    fun getAdapter(): ManageServicesAdapter {
        return adapter
    }

    fun getService(position: Int): ManageServicesDataModel {
        return serviceData[position]
    }

    internal fun getServices() {
        val token = StringBuilder("Bearer ")
                .append(readPreferences<String>(PreferencesKey.ACCESS_TOKEN))
                .toString()
        getCompositeDisposable().add(appRepository.getServices(this, token))
    }

    fun getServicesObservable() = servicesLiveData

    fun onItemClick(position: Int) {
        navigator.onMenuItemClicked(position)
    }
}
