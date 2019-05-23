package com.xjek.provider.views.currentorder_fragment

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesHelper
import com.xjek.base.data.PreferencesKey
import com.xjek.provider.models.TransportHistory
import com.xjek.provider.repository.AppRepository
import com.xjek.provider.utils.Constant
import com.xjek.xjek.ui.currentorder_fragment.CurrentOrderNavigator

class CurrentOrderViewModel : BaseViewModel<CurrentOrderNavigator>() {

    private val appRepository = AppRepository.instance()
    val preferenceHelper = PreferencesHelper

    var transportCurrentHistoryResponse = MutableLiveData<TransportHistory>()
    var loadingProgress = MutableLiveData<Boolean>()
    var errorResponse = MutableLiveData<String>()

    fun openDetailPage() {
        navigator.goToDetailPage()
    }


    fun getTransportCurrentHistory(selectedservice: String) {

        val hashMap: HashMap<String, String> = HashMap()
        hashMap.put("limit", "100")
        hashMap.put("offset", "0")
        hashMap.put("type", "current")
        getCompositeDisposable().add(appRepository
                .getTransaportCurrentHistory(this
                        , Constant.M_TOKEN + preferenceHelper.get(PreferencesKey.ACCESS_TOKEN, "")
                        , hashMap, selectedservice))

    }

    fun getServiceCurrentHistory(selectedservice: String) {

        val hashMap: HashMap<String, String> = HashMap()
        hashMap.put("limit", "100")
        hashMap.put("offset", "0")
        hashMap.put("type", "current")
        getCompositeDisposable().add(appRepository
                .getServiceCurrentHistory(this
                        , Constant.M_TOKEN + preferenceHelper.get(PreferencesKey.ACCESS_TOKEN, "")
                        , hashMap))
    }
}