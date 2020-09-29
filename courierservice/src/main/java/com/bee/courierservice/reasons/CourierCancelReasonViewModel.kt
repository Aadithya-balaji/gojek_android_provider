package com.bee.courierservice.reasons

import androidx.lifecycle.MutableLiveData
import com.bee.courierservice.model.ReasonModel
import com.bee.courierservice.repositary.CourierRepository
import com.gox.base.base.BaseViewModel
import com.gox.base.data.Constants
import com.gox.base.repository.ApiListener

class CourierCancelReasonViewModel : BaseViewModel<CourierCancelReasonNavigator>() {

    private val mRepository = CourierRepository.instance()
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