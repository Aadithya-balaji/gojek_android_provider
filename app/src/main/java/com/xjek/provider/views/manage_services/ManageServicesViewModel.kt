package com.xjek.provider.views.manage_services

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.provider.models.ManageServicesDataModel
import com.xjek.provider.models.ManageServicesResponseModel
import com.xjek.provider.repository.AppRepository

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
