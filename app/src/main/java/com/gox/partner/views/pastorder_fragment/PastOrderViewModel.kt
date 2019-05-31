package com.gox.xjek.ui.pastorder_fragment

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.data.PreferencesHelper
import com.gox.base.data.PreferencesKey
import com.gox.partner.models.HistoryModel
import com.gox.partner.repository.AppRepository
import com.gox.partner.utils.Constant

public class PastOrderViewModel : BaseViewModel<PastOrderNavigator>() {

    private val appRepository = AppRepository.instance()
    private val preferenceHelper = PreferencesHelper
    var historyResponseLiveData = MutableLiveData<HistoryModel>()
    var serviceList = MutableLiveData<ArrayList<HistoryModel.ResponseData.Service>>(ArrayList<HistoryModel.ResponseData.Service>())
    var orderList = MutableLiveData<ArrayList<HistoryModel.ResponseData.Order>>(ArrayList<HistoryModel.ResponseData.Order>())
    var taxiList = MutableLiveData<ArrayList<HistoryModel.ResponseData.Transport>>(ArrayList<HistoryModel.ResponseData.Transport>())
    var loadingProgress = MutableLiveData<Boolean>()
    var selectedServiceType = MutableLiveData<String>()
     var errorResponse = MutableLiveData<String>()

    fun openDetailPage() {
        navigator.gotoDetailPage()
    }

    fun getTransportPastHistory(selectedService: String) {
        val hashMap: HashMap<String, String> = HashMap()
        hashMap.put("limit", "100")
        hashMap.put("offset", "0")
        hashMap.put("type", "past")
        loadingProgress.value = true
        getCompositeDisposable().add(appRepository.getPastORderHistory(this, Constant.M_TOKEN + preferenceHelper.get(PreferencesKey.ACCESS_TOKEN, ""), hashMap, selectedService))
    }
}