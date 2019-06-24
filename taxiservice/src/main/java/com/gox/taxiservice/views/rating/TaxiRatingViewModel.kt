package com.gox.taxiservice.views.rating

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
import com.gox.taxiservice.model.TaxiRatingResponse
import com.gox.taxiservice.repositary.TaxiRepository

class TaxiRatingViewModel : BaseViewModel<TaxiRatingNavigator>() {

    private val mRepository = TaxiRepository.instance()
    var ratingLiveData = MutableLiveData<TaxiRatingResponse>()
    var showLoading = MutableLiveData<Boolean>()

    fun submitRating(params: HashMap<String, String>) {
        getCompositeDisposable().add(mRepository.submitRating(object : ApiListener {
            override fun success(successData: Any) {
                ratingLiveData.postValue(successData as TaxiRatingResponse)
                showLoading.postValue(false)
            }

            override fun fail(failData: Throwable) {
                navigator.showErrorMessage(getErrorMessage(failData))
                showLoading.postValue(false)
            }
        }, params))
    }
}