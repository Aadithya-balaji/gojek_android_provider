package com.gox.taxiservice.views.main

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.gox.base.base.BaseViewModel
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.taxiservice.model.*
import com.gox.taxiservice.repositary.TaxiRepository

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
    var distanceMeter = MutableLiveData(0.0)
    var locationPoint: List<LocationPoint> = listOf()
    var distanceApiProcessing = MutableLiveData<ArrayList<DistanceApiProcessing>>()

    init {
        distanceApiProcessing.value = arrayListOf()
    }

    var showLoading = MutableLiveData<Boolean>()

    fun callTaxiCheckStatusAPI() {
        if (latitude.value!! > 0 && longitude.value!! > 0)
            getCompositeDisposable()
                    .add(mRepository.checkRequest(
                            this,
                            "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN),
                            latitude.value!!,
                            longitude.value!!))
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

    fun taxiDroppingStatus(params: HashMap<String, String>) {
        val model = DroppedStatusModel()
//        try {
//            showLoading.value = true
//        } catch (e: Exception) {
//        }
        model.id = params["id"]!!
        model.status = params["status"]!!
        model._method = params["_method"]!!
        model.toll_price = params["toll_price"]!!
        model.latitude = latitude.value!!
        model.longitude = longitude.value!!
        model.location_points = locationPoint

//        getCompositeDisposable().add(mRepository.taxiDroppingStatus
//        (this, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), model))
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