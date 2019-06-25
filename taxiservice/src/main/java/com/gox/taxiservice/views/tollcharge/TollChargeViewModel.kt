package com.gox.taxiservice.views.tollcharge

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
import com.gox.taxiservice.model.CheckRequestModel
import com.gox.taxiservice.model.DroppedStatusModel
import com.gox.taxiservice.repositary.TaxiRepository

class TollChargeViewModel : BaseViewModel<TollChargeNavigator>() {

    private val mRepository = TaxiRepository.instance()

    var mLiveData = MutableLiveData<CheckRequestModel>()
    var tollChargeLiveData = MutableLiveData<String>()
    var showLoading = MutableLiveData<Boolean>()

    fun callUpdateRequestApi(model: DroppedStatusModel) {
        mRepository.updateRequest(object : ApiListener {
            override fun success(successData: Any) {
                mLiveData.postValue(successData as CheckRequestModel)
                showLoading.postValue(false)
            }

            override fun fail(failData: Throwable) {
                navigator.showErrorMessage(getErrorMessage(failData))
                showLoading.postValue(false)
            }
        }, model)
    }
}