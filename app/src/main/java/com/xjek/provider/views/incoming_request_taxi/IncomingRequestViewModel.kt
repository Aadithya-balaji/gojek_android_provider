package com.xjek.provider.views.incoming_request_taxi

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.provider.models.AcceptRequestModel
import com.xjek.provider.models.RejectRequestModel
import com.xjek.provider.repository.AppRepository

class  IncomingRequestViewModel:BaseViewModel<IncomingNavigator>(){
    val appRepository = AppRepository.instance()

    var pickupLocation=MutableLiveData<String>()
    var serviceType=MutableLiveData<String>()
    var timeLeft= MutableLiveData<String>()
    var  acceptRequestLiveData=MutableLiveData<AcceptRequestModel>()
    var rejectRequestLiveData=MutableLiveData<RejectRequestModel>()
    var showLoading=MutableLiveData<Boolean>()

    fun acceptReq(){
        navigator.accept()
    }

    fun cancelReq(){
        navigator.cancel()
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