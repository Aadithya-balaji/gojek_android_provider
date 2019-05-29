package com.gox.partner.views.incoming_request_taxi

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.partner.models.AcceptRequestModel
import com.gox.partner.models.RejectRequestModel
import com.gox.partner.repository.AppRepository

class IncomingRequestViewModel : BaseViewModel<IncomingNavigator>() {

    val appRepository = AppRepository.instance()

    var pickupLocation = MutableLiveData<String>()
    var serviceType = MutableLiveData<String>()
    var timeLeft = MutableLiveData<String>()
    var acceptRequestLiveData = MutableLiveData<AcceptRequestModel>()
    var rejectRequestLiveData = MutableLiveData<RejectRequestModel>()
    var showLoading = MutableLiveData<Boolean>()

    fun acceptReq() {
        navigator.accept()
    }

    fun cancelReq() {
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