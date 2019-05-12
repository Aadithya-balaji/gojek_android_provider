package com.xjek.xjek.ui.pastorder_fragment

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesHelper
import com.xjek.base.data.PreferencesKey
import com.xjek.provider.models.HistoryModel
import com.xjek.provider.repository.AppRepository
import com.xjek.provider.utils.Constant

public class PastOrderViewModel : BaseViewModel<PastOrderNavigator>() {

    private val appRepository = AppRepository.instance()
    private val preferenceHelper = PreferencesHelper
    private var transportHistoryResponse = MutableLiveData<HistoryModel>()
    private var serviceList = MutableLiveData<List<HistoryModel.ResponseData.Service>>()
    private var orderList = MutableLiveData<List<HistoryModel.ResponseData.Order>>()
    private var taxiList = MutableLiveData<List<HistoryModel.ResponseData.Transport>>()
    private var loadingProgress = MutableLiveData<Boolean>()
    private var errorResponse = MutableLiveData<String>()

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