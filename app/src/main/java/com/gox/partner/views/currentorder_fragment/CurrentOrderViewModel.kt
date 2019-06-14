package com.gox.partner.views.currentorder_fragment

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.data.Constants
import com.gox.base.data.PreferencesHelper
import com.gox.base.data.PreferencesKey
import com.gox.partner.models.TransportHistory
import com.gox.partner.repository.AppRepository

class CurrentOrderViewModel : BaseViewModel<CurrentOrderNavigator>() {

    private val mRepository = AppRepository.instance()
    private val mPreferenceHelper = PreferencesHelper

    var transportCurrentHistoryResponse = MutableLiveData<TransportHistory>()
    var loadingProgress = MutableLiveData<Boolean>()
    var errorResponse = MutableLiveData<String>()

    fun getTransportCurrentHistory(s: String) {
        val hashMap: HashMap<String, String> = HashMap()
        hashMap["limit"] = "100"
        hashMap["offset"] = "0"
        hashMap["type"] = "current"
        getCompositeDisposable().add(mRepository
                .getTransaportCurrentHistory(this
                        , Constants.M_TOKEN + mPreferenceHelper.get(PreferencesKey.ACCESS_TOKEN, "")
                        , hashMap, s))
    }
}