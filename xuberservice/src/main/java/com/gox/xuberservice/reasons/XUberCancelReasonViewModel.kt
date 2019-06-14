package com.gox.xuberservice.reasons

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.data.Constants
import com.gox.base.repository.ApiListener
import com.gox.xuberservice.model.ReasonModel
import com.gox.xuberservice.repositary.XUberRepository

class XUberCancelReasonViewModel : BaseViewModel<XUberCancelReasonNavigator>() {

    private val mRepository = XUberRepository.instance()
    val mReasonResponseData = MutableLiveData<ReasonModel>()
    var errorResponse = MutableLiveData<Throwable>()

    fun getReason() {
        getCompositeDisposable().add(mRepository.xUberGetReason(object : ApiListener {
            override fun success(successData: Any) {
                mReasonResponseData.value = successData as ReasonModel
            }

            override fun fail(failData: Throwable) {
                navigator.getErrorMessage(getErrorMessage(failData))
            }
        }, Constants.ModuleTypes.SERVICE))
    }
}