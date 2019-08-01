package com.gox.partner.views.pastorder_fragment

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
import com.gox.partner.models.HistoryModel
import com.gox.partner.repository.AppRepository

class PastOrderViewModel : BaseViewModel<PastOrderNavigator>() {

    private val mRepository = AppRepository.instance()

    var historyResponseLiveData = MutableLiveData<HistoryModel>()
    var serviceList = MutableLiveData<ArrayList<HistoryModel.ResponseData.Service>>()
    var orderList = MutableLiveData<ArrayList<HistoryModel.ResponseData.Order>>()
    var taxiList = MutableLiveData<ArrayList<HistoryModel.ResponseData.Transport>>()
    var showLoading = MutableLiveData<Boolean>()
    var selectedServiceType = MutableLiveData<String>()
    var errorResponse = MutableLiveData<String>()

    fun getTransportPastHistory(selectedService: String,offset: String) {
        val hashMap: HashMap<String, String> = HashMap()
        hashMap["limit"] = "100"
        hashMap["offset"] = offset
        hashMap["type"] = "past"
        showLoading.value = true
        getCompositeDisposable().add(mRepository.getPastOrderHistory(object : ApiListener {
            override fun success(successData: Any) {
                historyResponseLiveData.value = successData as HistoryModel
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = getErrorMessage(failData)
            }
        }, hashMap, selectedService))
    }
}