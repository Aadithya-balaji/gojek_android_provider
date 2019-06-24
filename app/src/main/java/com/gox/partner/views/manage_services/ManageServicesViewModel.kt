package com.gox.partner.views.manage_services

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
import com.gox.partner.models.ManageServicesDataModel
import com.gox.partner.models.ManageServicesResponseModel
import com.gox.partner.repository.AppRepository

class ManageServicesViewModel : BaseViewModel<ManageServicesNavigator>() {

    private val mRepository = AppRepository.instance()
    private val servicesLiveData = MutableLiveData<ManageServicesResponseModel>()

    private var serviceData: List<ManageServicesDataModel> = emptyList()
    private val mAdapter: ManageServicesAdapter = ManageServicesAdapter(this)

    fun setServiceData(serviceData: List<ManageServicesDataModel>) {
        this.serviceData = serviceData
    }

    fun getServiceData() = serviceData

    fun setAdapter() = mAdapter.notifyDataSetChanged()

    fun getAdapter() = mAdapter

    fun getService(position: Int) = serviceData[position]

    fun getServices() {
        getCompositeDisposable().add(mRepository.getServices(object : ApiListener {
            override fun success(successData: Any) {
                getServicesObservable().value = successData as ManageServicesResponseModel
            }

            override fun fail(failData: Throwable) {
                navigator.showError(getErrorMessage(failData))
            }
        }))
    }

    fun getServicesObservable() = servicesLiveData

    fun onItemClick(position: Int) = navigator.onMenuItemClicked(position)
}
