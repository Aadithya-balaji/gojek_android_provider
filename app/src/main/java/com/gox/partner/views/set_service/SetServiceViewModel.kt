package com.gox.partner.views.set_service

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
import com.gox.partner.models.ServiceCategoriesResponse
import com.gox.partner.repository.AppRepository

class SetServiceViewModel : BaseViewModel<SetServiceNavigator>() {

    private val mRepository = AppRepository.instance()
    val serviceCategoriesResponse = MutableLiveData<ServiceCategoriesResponse>()
    val errorResponse = MutableLiveData<String>()

    fun getCategories() {
        getCompositeDisposable().add(mRepository.getServiceCategories(object : ApiListener {
            override fun success(successData: Any) {
                serviceCategoriesResponse.value = successData as ServiceCategoriesResponse
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = getErrorMessage(failData)
            }
        }))
    }
}
