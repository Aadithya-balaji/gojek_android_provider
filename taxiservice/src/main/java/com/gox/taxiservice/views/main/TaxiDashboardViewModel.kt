package com.gox.taxiservice.views.main

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.gox.base.base.BaseApplication
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
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
    var locationPoint: ArrayList<LocationPoint> = arrayListOf()
    var distanceApiProcessing = MutableLiveData<ArrayList<DistanceApiProcessing>>()
    var iteratePointsForApi = ArrayList<LatLng>()

    var tempSrc = MutableLiveData<LatLng>()
    var tempDest = MutableLiveData<LatLng>()

    init {
        distanceApiProcessing.value = arrayListOf()
    }

    var showLoading = MutableLiveData<Boolean>()

    fun callTaxiCheckStatusAPI() {
        if (BaseApplication.isNetworkAvailable)
            if (latitude.value!! > 0 && longitude.value!! > 0)
                getCompositeDisposable().add(mRepository.checkRequest(object : ApiListener {
                    override fun success(successData: Any) {
                        checkStatusTaxiLiveData.value = successData as CheckRequestModel
                        showLoading.value = false
                    }

                    override fun fail(failData: Throwable) {
                        navigator.showErrorMessage(getErrorMessage(failData))
                        showLoading.value = false
                    }
                }))
            else navigator.updateCurrentLocation()
    }

    fun taxiStatusUpdate(params: HashMap<String, String>) {
        if (BaseApplication.isNetworkAvailable) {
            showLoading.value = true
            getCompositeDisposable().add(mRepository.taxiStatusUpdate(object : ApiListener {
                override fun success(successData: Any) {
                    callTaxiCheckStatusAPI()
                    showLoading.value = false
                }

                override fun fail(failData: Throwable) {
                    navigator.showErrorMessage(getErrorMessage(failData))
                    showLoading.value = false
                }
            }, params))
        }
    }

    fun taxiDroppingStatus(params: HashMap<String, String>) {
        if (BaseApplication.isNetworkAvailable) {
            val model = DroppedStatusModel()
            try {
                showLoading.value = true
            } catch (e: Exception) {
                e.printStackTrace()
            }

            for (points in iteratePointsForApi)
                locationPoint.add(LocationPoint(points.latitude, points.longitude))

            model.id = params["id"]!!
            model.status = params["status"]!!
            model._method = params["_method"]!!
            model.toll_price = params["toll_price"]!!
            model.distance = distanceMeter.value!!
            model.latitude = latitude.value!!
            model.longitude = longitude.value!!
            model.location_points = locationPoint

            getCompositeDisposable().add(mRepository.taxiDroppingStatus(object : ApiListener {
                override fun success(successData: Any) {
                    callTaxiCheckStatusAPI()
                    showLoading.value = false
                }

                override fun fail(failData: Throwable) {
                    navigator.showErrorMessage(getErrorMessage(failData))
                    showLoading.value = false
                }
            }, model))
        }
    }

    fun taxiWaitingTime(params: HashMap<String, String>) {
        if (BaseApplication.isNetworkAvailable)
            getCompositeDisposable().add(mRepository.waitingTime(object : ApiListener {
                override fun success(successData: Any) {
                    waitingTimeLiveData.value = successData as WaitingTime
                    showLoading.value = false
                }

                override fun fail(failData: Throwable) {
                    navigator.showErrorMessage(getErrorMessage(failData))
                    showLoading.value = false
                }
            }, params))
    }

    fun cancelRequest(params: HashMap<String, String>) {
        if (BaseApplication.isNetworkAvailable)
            getCompositeDisposable().add(mRepository.taxiCancelReason(object : ApiListener {
                override fun success(successData: Any) {
                    taxiCancelRequest.value = successData as CancelRequestModel
                    showLoading.value = false
                }

                override fun fail(failData: Throwable) {
                    navigator.showErrorMessage(getErrorMessage(failData))
                    showLoading.value = false
                }
            }, params))
    }
}