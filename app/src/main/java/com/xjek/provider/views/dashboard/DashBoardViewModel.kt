package com.xjek.provider.views.dashboard

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.provider.models.CheckRequestModel
import com.xjek.provider.models.ProfileResponse
import com.xjek.provider.repository.AppRepository
import com.xjek.provider.utils.Constant

class DashBoardViewModel : BaseViewModel<DashBoardNavigator>() {

    val appRepository = AppRepository.instance()

    var checkRequestLiveData = MutableLiveData<CheckRequestModel>()
    var mProfileResponse = MutableLiveData<ProfileResponse>()

    var latitude = MutableLiveData<Double>()
    var longitude = MutableLiveData<Double>()
    var selectedFilterService = MutableLiveData<String>()
    var currentStatus = MutableLiveData<String>()

    fun callCheckStatusAPI() {
//        if (latitude.value!! > 0 && longitude.value!! > 0) {
        if (latitude.value!!.toInt() > 0 && longitude.value!!.toInt() > 0) {
            if (readPreferences<String>(PreferencesKey.ACCESS_TOKEN).length > 2)
                getCompositeDisposable().add(appRepository.checkRequest(this,
                        "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN),
                        latitude.value.toString(),
                        longitude.value.toString())
                )
        } else navigator.updateCurrentLocation()
    }

    fun getProfile() {
        getCompositeDisposable().add(appRepository.getProviderProfile(this,
                Constant.M_TOKEN + readPreferences(PreferencesKey.ACCESS_TOKEN, "").toString()))
    }
}
