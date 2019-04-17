package com.xjek.provider.views.home

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.provider.models.AcceptRequestModel
import com.xjek.provider.models.CheckRequestModel
import com.xjek.provider.models.RejectRequestModel
import com.xjek.provider.repository.AppRepository

class HomeViewModel : BaseViewModel<HomeNavigator>() {

    val appRepository = AppRepository.instance()

    var checkRequestLiveData = MutableLiveData<CheckRequestModel>()
    var acceptRequestLiveData = MutableLiveData<AcceptRequestModel>()
    var rejectRequestLiveData = MutableLiveData<RejectRequestModel>()

    var showLoading = MutableLiveData<Boolean>()
    var latitude = MutableLiveData<Double>()
    var longitude = MutableLiveData<Double>()

    fun opentTranxitModule() {
        navigator.gotoTaxiModule()
    }

    fun openFoodieModule() {
        navigator.gotoFoodieModule()
    }

    fun openXuberMoudle() {
        navigator.gotoXuberModule()
    }

    fun changeStatus(view: View) {
        navigator.changeStatus(view)
    }

    fun getRequest() {
        getCompositeDisposable()
                .add(appRepository.checkRequest(
                        this,
                        "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN),
                        latitude.value.toString(),
                        longitude.value.toString())
                )
    }

    fun acceptRequest(param: HashMap<String, String>) {
        getCompositeDisposable()
                .add(appRepository.acceptIncomingRequest(
                        this,
                        "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN),
                        param)
                )
    }

    fun rejectRequest(param: HashMap<String, String>) {
        getCompositeDisposable()
                .add(appRepository.rejectIncomingRequest(
                        this,
                        "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN),
                        param)
                )
    }

}
