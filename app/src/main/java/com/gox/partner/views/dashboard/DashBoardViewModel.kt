package com.gox.partner.views.dashboard

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.Api
import com.gox.base.base.BaseApplication
import com.gox.base.base.BaseViewModel
import com.gox.base.data.PreferencesHelper
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.base.repository.ApiListener
import com.gox.partner.models.AirportChangeResponseModel
import com.gox.partner.models.CheckRequestModel
import com.gox.partner.models.ProfileResponse
import com.gox.partner.repository.AppRepository

class DashBoardViewModel : BaseViewModel<DashBoardNavigator>() {

    val mRepository = AppRepository.instance()
    var checkRequestLiveData = MutableLiveData<CheckRequestModel>()
    var mProfileResponse = MutableLiveData<ProfileResponse>()
    var latitude = MutableLiveData<Double>()
    var longitude = MutableLiveData<Double>()
    var selectedFilterService = MutableLiveData<String>()
    var currentStatus = MutableLiveData<String>()
    var airportModeResponse=MutableLiveData<AirportChangeResponseModel>()
    var loaderProgress=MutableLiveData<Boolean>()

    fun callCheckStatusAPI() {
        if (BaseApplication.isNetworkAvailable)
            if (latitude.value!! != 0.0 && longitude.value!! != 0.0) {
                if (readPreferences<String>(PreferencesKey.ACCESS_TOKEN).length > 2)
                    getCompositeDisposable().add(mRepository.checkRequest(object : ApiListener {
                        override fun success(successData: Any) {
                            checkRequestLiveData.value = successData as CheckRequestModel
                        }

                        override fun fail(failData: Throwable) {
                            navigator.showErrorMessage(getErrorMessage(failData))
                        }
                    }, latitude.value.toString(), longitude.value.toString()))
            } else navigator.updateCurrentLocation()
    }

    fun getProfile() {
        if (BaseApplication.isNetworkAvailable)
            getCompositeDisposable().add(mRepository.getProviderProfile(object : ApiListener {
                override fun success(successData: Any) {
                    mProfileResponse.value = successData as ProfileResponse
                }

                override fun fail(failData: Throwable) {
                }
            }))
    }

    fun changeAirportModel() {
       // loaderProgress.postValue(true)
        if (BaseApplication.isNetworkAvailable)
            getCompositeDisposable().add(mRepository.setAirportMode(object : ApiListener {
                override fun success(successData: Any) {
                    loaderProgress.postValue(false)
                    airportModeResponse.postValue(successData as AirportChangeResponseModel)
                }

                override fun fail(failData: Throwable) {
                     loaderProgress.postValue(false)
                }

            }))
    }
}
