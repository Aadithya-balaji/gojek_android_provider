package com.gox.partner.views.manage_documents

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
import com.gox.partner.models.ManageServicesResponseModel
import com.gox.partner.repository.AppRepository

class ManageDocumentsViewModel : BaseViewModel<ManageDocumentsNavigator>() {

    private val mRepository = AppRepository.instance()
    val servicesLiveData = MutableLiveData<ManageServicesResponseModel>()
    val errorLiveData = MutableLiveData<String>()

    fun getServices() {
        getCompositeDisposable().add(mRepository.getServices(object : ApiListener {
            override fun success(successData: Any) {
                servicesLiveData.value = successData as ManageServicesResponseModel
            }

            override fun fail(failData: Throwable) {
               errorLiveData.value =getErrorMessage(failData)
            }
        }))
    }

}