package com.xjek.taxiservice.views.main

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.taxiservice.model.CancelRequestModel
import com.xjek.taxiservice.model.CheckRequestModel
import com.xjek.taxiservice.model.WaitingTime
import com.xjek.taxiservice.repositary.TaxiRepository

class TaxiDashboardViewModel : BaseViewModel<TaxiDashboardNavigator>() {

    private val mRepository = TaxiRepository.instance()

    var waitingTimeLiveData = MutableLiveData<WaitingTime>()
    var checkStatusTaxiLiveData = MutableLiveData<CheckRequestModel>()
    var taxiCancelRequest = MutableLiveData<CancelRequestModel>()
    var polyLineSrc = MutableLiveData<LatLng>()
    var polyLineDest = MutableLiveData<LatLng>()
    var currentStatus = MutableLiveData<String>()

    var latitude = MutableLiveData<Double>()
    var longitude = MutableLiveData<Double>()

    var showLoading = MutableLiveData<Boolean>()

    fun callTaxiCheckStatusAPI() {
        if (latitude.value!! > 0 && longitude.value!! > 0)
            getCompositeDisposable().add(mRepository.checkRequest(this,
                    "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN),
                    latitude.value!!, longitude.value!!)
            )
        else navigator.updateCurrentLocation()
    }

    fun taxiStatusUpdate(params: HashMap<String, String>) {
        try {
            showLoading.value = true
        } catch (e: Exception) {
        }
        getCompositeDisposable().add(mRepository.taxiStatusUpdate
        (this, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), params))
    }

    fun taxiWaitingTime(params: HashMap<String, String>) {
        getCompositeDisposable().add(mRepository.waitingTime(this,
                "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), params))
    }

    fun cancelRequest(params: HashMap<String, String>) {
        getCompositeDisposable().add(mRepository.taxiCancelReason(this,
                "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), params))
    }

}