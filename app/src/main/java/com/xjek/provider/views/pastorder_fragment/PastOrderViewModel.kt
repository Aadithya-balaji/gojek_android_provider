package com.xjek.xjek.ui.pastorder_fragment

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesHelper
import com.xjek.base.data.PreferencesKey
import com.xjek.provider.model.TransportHistory
import com.xjek.provider.repository.AppRepository
import com.xjek.provider.utils.Constant

public class PastOrderViewModel : BaseViewModel<PastOrderNavigator>() {

    private val appRepository = AppRepository.instance()
    val preferenceHelper = PreferencesHelper

    var transportHistoryResponse = MutableLiveData<TransportHistory>()
    var loadingProgress = MutableLiveData<Boolean>()
    var errorResponse = MutableLiveData<String>()


    fun openDetailPage() {
        navigator.gotoDetailPage()

    }

    fun getTransportPastHistory(selectedService : String) {

        val hashMap: HashMap<String, String> = HashMap()
        hashMap.put("limit", "100")
        hashMap.put("offset", "0")
        hashMap.put("type", "past")

        loadingProgress.value = true
        getCompositeDisposable().add(appRepository
                .getTransaportHistory(this
                        , Constant.M_TOKEN + preferenceHelper.get(PreferencesKey.ACCESS_TOKEN, "")
                        , hashMap,selectedService))

    }
}