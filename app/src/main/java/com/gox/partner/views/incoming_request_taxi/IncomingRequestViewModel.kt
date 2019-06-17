package com.gox.partner.views.incoming_request_taxi

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
import com.gox.partner.models.AcceptRequestModel
import com.gox.partner.models.RejectRequestModel
import com.gox.partner.repository.AppRepository

class IncomingRequestViewModel : BaseViewModel<IncomingNavigator>() {

    val mRepository = AppRepository.instance()

    var pickupLocation = MutableLiveData<String>()
    var serviceType = MutableLiveData<String>()
    var acceptRequestLiveData = MutableLiveData<AcceptRequestModel>()
    var rejectRequestLiveData = MutableLiveData<RejectRequestModel>()
    var showLoading = MutableLiveData<Boolean>()

    fun acceptReq() = navigator.accept()

    fun cancelReq() = navigator.cancel()

    fun acceptRequest(param: HashMap<String, String>) {
        getCompositeDisposable().add(mRepository.acceptIncomingRequest(object : ApiListener {
            override fun success(successData: Any) {
                acceptRequestLiveData.value = successData as AcceptRequestModel
            }

            override fun fail(failData: Throwable) {
                navigator.showErrorMessage(getErrorMessage(failData))
            }
        }, param))
    }

    fun rejectRequest(param: HashMap<String, String>) {
        getCompositeDisposable().add(mRepository.rejectIncomingRequest(object : ApiListener {
            override fun success(successData: Any) {
                rejectRequestLiveData.value = successData as RejectRequestModel
            }

            override fun fail(failData: Throwable) {
                navigator.showErrorMessage(getErrorMessage(failData))
            }
        }, param))
    }

}