/*
package com.gox.partner.views.currentorder_fragment

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
import com.gox.partner.models.TransportHistory
import com.gox.partner.repository.AppRepository

class CurrentOrderViewModel : BaseViewModel<CurrentOrderNavigator>() {

    private val mRepository = AppRepository.instance()

    var transportCurrentHistoryResponse = MutableLiveData<TransportHistory>()
    var showLoading = MutableLiveData<Boolean>()
    var errorResponse = MutableLiveData<String>()

    fun getTransportCurrentHistory(s: String) {
        val hashMap: HashMap<String, String> = HashMap()
        hashMap["limit"] = "100"
        hashMap["offset"] = "0"
        hashMap["type"] = "current"
        getCompositeDisposable().add(mRepository.getTransportCurrentHistory(object : ApiListener {
            override fun success(successData: Any) {
                transportCurrentHistoryResponse.value = successData as TransportHistory
                showLoading.postValue(false)
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = getErrorMessage(failData)
                showLoading.postValue(false)
            }
        }, hashMap, s))
    }
}*/
