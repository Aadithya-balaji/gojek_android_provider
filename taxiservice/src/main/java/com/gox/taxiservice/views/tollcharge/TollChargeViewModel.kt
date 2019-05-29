package com.gox.taxiservice.views.tollcharge

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.data.Constants.RideStatus.DROPPED
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.taxiservice.model.CheckRequestModel
import com.gox.taxiservice.repositary.TaxiRepository

class TollChargeViewModel : BaseViewModel<TollChargeNavigator>() {

    val appRepository: TaxiRepository = TaxiRepository.instance()
    var tollChargeLiveData = MutableLiveData<String>()
    var mLiveData = MutableLiveData<CheckRequestModel>()
    var showLoading = MutableLiveData<Boolean>()
    var requestID = MutableLiveData<String>()

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