package com.gox.taxiservice.views.reasons

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
import com.gox.taxiservice.model.ReasonModel
import com.gox.taxiservice.repositary.TaxiRepository

class TaxiCancelReasonViewModel : BaseViewModel<TaxiCancelReasonNavigator>() {

    private val mRepository = TaxiRepository.instance()
    val mResponse = MutableLiveData<ReasonModel>()

    fun dismissPopup() = navigator.closePopup()

    fun getReason() {
        getCompositeDisposable().add(mRepository.taxiGetReason(object : ApiListener {
            override fun success(successData: Any) {
                mResponse.value = successData as ReasonModel
            }

            override fun fail(failData: Throwable) {
            }
        }))
    }
}