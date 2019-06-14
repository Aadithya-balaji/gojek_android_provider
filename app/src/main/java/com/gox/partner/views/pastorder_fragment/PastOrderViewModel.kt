package com.gox.partner.views.pastorder_fragment

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.data.Constants
import com.gox.base.data.PreferencesHelper
import com.gox.base.data.PreferencesKey
import com.gox.partner.models.HistoryModel
import com.gox.partner.repository.AppRepository

class PastOrderViewModel : BaseViewModel<PastOrderNavigator>() {

    private val appRepository = AppRepository.instance()
    private val preferenceHelper = PreferencesHelper

    var historyResponseLiveData = MutableLiveData<HistoryModel>()
    var serviceList = MutableLiveData<ArrayList<HistoryModel.ResponseData.Service>>(ArrayList())
    var orderList = MutableLiveData<ArrayList<HistoryModel.ResponseData.Order>>(ArrayList())
    var taxiList = MutableLiveData<ArrayList<HistoryModel.ResponseData.Transport>>(ArrayList())
    var loadingProgress = MutableLiveData<Boolean>()
    var selectedServiceType = MutableLiveData<String>()
    var errorResponse = MutableLiveData<String>()

    fun getTransportPastHistory(selectedService: String) {
        val hashMap: HashMap<String, String> = HashMap()
        hashMap["limit"] = "100"
        hashMap["offset"] = "0"
        hashMap["type"] = "past"
        loadingProgress.value = true
        getCompositeDisposable().add(appRepository.getPastORderHistory(this,
                Constants.M_TOKEN + preferenceHelper.get(PreferencesKey.ACCESS_TOKEN, ""), hashMap, selectedService))
    }
}