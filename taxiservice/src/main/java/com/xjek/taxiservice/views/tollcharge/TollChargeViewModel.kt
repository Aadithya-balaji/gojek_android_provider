package com.xjek.taxiservice.views.tollcharge

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.Constants.RideStatus.DROPPED
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.taxiservice.model.CheckRequestModel
import com.xjek.taxiservice.repositary.TaxiRepository

class TollChargeViewModel : BaseViewModel<TollChargeNavigator>() {
    val appRepository: TaxiRepository = TaxiRepository.instance()
    var tollChargeLiveData = MutableLiveData<String>()
    var updateRequestLiveData = MutableLiveData<CheckRequestModel>()
    var showLoading = MutableLiveData<Boolean>()
    var requestID = MutableLiveData<String>()

    fun submit() {
        navigator.addTollCharge()
    }


    fun dismisss() {
        navigator.dismissDialog()
    }

    fun callUpdateRequestApi() {
        if (navigator.isValidCharge()) {
            showLoading.value = true
            val params = HashMap<String, String>()
            params.put("id", requestID.value.toString())
            params.put("status", DROPPED)
            params.put("_method", "PATCH")
            appRepository.updateRequest(this,"Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), params)
        }
    }
}