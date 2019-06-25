package com.gox.partner.views.manage_documents

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
import com.gox.partner.models.ManageServicesResponseModel
import com.gox.partner.repository.AppRepository

class ManageDocumentsViewModel : BaseViewModel<ManageDocumentsNavigator>() {

    private val mRepository = AppRepository.instance()
    val servicesLiveData = MutableLiveData<ManageServicesResponseModel>()

    fun showAllDocuments() = navigator.showAllDocuments()
    fun showTransportDocuments() = navigator.showTransportDocuments()
    fun showDeliveryDocuments() = navigator.showDeliveryDocuments()
    fun showServicesDocuments() = navigator.showServicesDocuments()

    fun getServices() {
        getCompositeDisposable().add(mRepository.getServices(object : ApiListener {
            override fun success(successData: Any) {
                servicesLiveData.value = successData as ManageServicesResponseModel
            }

            override fun fail(failData: Throwable) {
                navigator.showError(getErrorMessage(failData))
            }
        }))
    }

}