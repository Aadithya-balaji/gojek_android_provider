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
    var mLiveData = MutableLiveData<CheckRequestModel>()
    var showLoading = MutableLiveData<Boolean>()
    var requestID = MutableLiveData<String>()

    fun submit() {
        navigator.addTollCharge()
    }

    fun callUpdateRequestApi() {
        if (navigator.isValidCharge()) {
            showLoading.value = true
            val params = HashMap<String, String>()
            params["id"] = requestID.value.toString()
            params["status"] = DROPPED
            params["_method"] = "PATCH"
            params["toll_price"] = tollChargeLiveData.value!!
            appRepository.updateRequest(this, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), params)
        }
    }
}