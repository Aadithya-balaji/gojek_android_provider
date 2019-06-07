package com.gox.partner.views.currentorder_fragment

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.data.Constants
import com.gox.base.data.PreferencesHelper
import com.gox.base.data.PreferencesKey
import com.gox.partner.models.TransportHistory
import com.gox.partner.repository.AppRepository
import com.gox.xjek.ui.currentorder_fragment.CurrentOrderNavigator

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
                        , Constants.M_TOKEN + preferenceHelper.get(PreferencesKey.ACCESS_TOKEN, "")
                        , hashMap, selectedservice))

    }

    fun getServiceCurrentHistory(selectedservice: String) {

        val hashMap: HashMap<String, String> = HashMap()
        hashMap.put("limit", "100")
        hashMap.put("offset", "0")
        hashMap.put("type", "current")
        getCompositeDisposable().add(appRepository
                .getServiceCurrentHistory(this
                        , Constants.M_TOKEN + preferenceHelper.get(PreferencesKey.ACCESS_TOKEN, "")
                        , hashMap))
    }
}