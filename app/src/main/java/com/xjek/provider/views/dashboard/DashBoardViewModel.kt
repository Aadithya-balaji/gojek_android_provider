package com.xjek.provider.views.dashboard

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.provider.models.CheckRequestModel
import com.xjek.provider.repository.AppRepository

class DashBoardViewModel :BaseViewModel<DashBoardNavigator>(){

    val appRepository = AppRepository.instance()

    var checkRequestLiveData = MutableLiveData<CheckRequestModel>()

    var latitude = MutableLiveData<Double>()
    var longitude = MutableLiveData<Double>()

    fun callCheckStatusAPI() {
        getCompositeDisposable()
                .add(appRepository.checkRequest(
                        this,
                        "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN),
                        latitude.value.toString(),
                        longitude.value.toString())
                )
    }
}
