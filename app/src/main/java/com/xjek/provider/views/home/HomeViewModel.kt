package com.xjek.provider.views.home

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.provider.models.CheckRequetModel
import com.xjek.provider.models.StatusResponseModel
import com.xjek.provider.repository.AppRepository

class HomeViewModel : BaseViewModel<Home_Navigator>() {

    val appRepository = AppRepository.instance()
    var checkRequestLiveData = MutableLiveData<CheckRequetModel>()
    var showLoading = MutableLiveData<Boolean>()
    var latitude = MutableLiveData<Double>()
    var longitude = MutableLiveData<Double>()
    var onlineStatusLiveData=MutableLiveData<StatusResponseModel>()

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
        getCompositeDisposable().add(appRepository.checkRequest(this,
                "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), latitude.value.toString(), longitude.value.toString()))
    }


    fun changeOnlineStatus(status:String){
        getCompositeDisposable().add(appRepository.changeOnlineStatus(this,"Bearer "+readPreferences<String>(PreferencesKey.ACCESS_TOKEN),status))
    }
}
