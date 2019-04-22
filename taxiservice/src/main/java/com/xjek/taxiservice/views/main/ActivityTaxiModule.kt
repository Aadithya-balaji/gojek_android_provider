package com.xjek.taxiservice.views.main

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.taxiservice.model.CheckRequestModel
import com.xjek.taxiservice.model.WaitingTime
import com.xjek.taxiservice.repositary.TaxiRepository

class ActivityTaxiModule : BaseViewModel<ActivityTaxMainNavigator>() {

    private val mRepository = TaxiRepository.instance()
      var waitingTimeLiveData=MutableLiveData<WaitingTime>()

    var checkStatusTaxiLiveData = MutableLiveData<CheckRequestModel>()

    var latitude = MutableLiveData<Double>()
    var longitude = MutableLiveData<Double>()

    fun callTaxiCheckStatusAPI() {
        getCompositeDisposable().add(mRepository.checkRequest(
                this,
                "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN),
                latitude.value.toString(),
                longitude.value.toString())
        )
    }

    fun taxiStatusUpdate(params: HashMap<String, String>) {
        getCompositeDisposable().add(mRepository.taxiStatusUpdate
        (this, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), params))
    }

}
