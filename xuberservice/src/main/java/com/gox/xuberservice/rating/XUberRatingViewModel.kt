package com.gox.xuberservice.rating

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
import com.gox.xuberservice.model.XuperRatingModel
import com.gox.xuberservice.repositary.XUberRepository

class XUberRatingViewModel : BaseViewModel<XUberRatingNavigator>() {

    private val mRepository = XUberRepository.instance()

    val ratingLiveData = MutableLiveData<XuperRatingModel>()
    var comment = MutableLiveData<String>()
    val rating = MutableLiveData<String>()
    val id = MutableLiveData<String>()
    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()
    var showLoading = MutableLiveData<Boolean>()
    var userRating = MutableLiveData<String>()

    fun callRateApi() {
        showLoading.value = true
        navigator.submitRating()
    }

    fun callRatingApi(params: HashMap<String, String>) {
        getCompositeDisposable().add(mRepository.xUberRatingUser(object : ApiListener {
            override fun success(successData: Any) {
                ratingLiveData.value = successData as XuperRatingModel
                showLoading.postValue(false)
            }

            override fun fail(failData: Throwable) {
                navigator.showErrorMessage(getErrorMessage(failData))
                showLoading.postValue(false)
            }
        }, params))
    }

}