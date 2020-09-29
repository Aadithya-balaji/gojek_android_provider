package com.bee.courierservice.rating

import androidx.lifecycle.MutableLiveData
import com.bee.courierservice.model.CourierRatingModel
import com.bee.courierservice.repositary.CourierRepository
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener

class CourierRatingViewModel : BaseViewModel<CourierRatingNavigator>() {

    private val mRepository = CourierRepository.instance()

    val ratingLiveData = MutableLiveData<CourierRatingModel>()
    var comment = MutableLiveData<String>().apply { setValue("") }
    val rating = MutableLiveData<String>()
    val id = MutableLiveData<String>()
    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()
    var showLoading = MutableLiveData<Boolean>()
    var userRating = MutableLiveData<String>().apply { setValue("1") }

    fun callRateApi() {
        showLoading.value = true
        navigator.submitRating()
    }

    fun callRatingApi(params: HashMap<String, String>) {
        getCompositeDisposable().add(mRepository.xUberRatingUser(object : ApiListener {
            override fun success(successData: Any) {
                ratingLiveData.value = successData as CourierRatingModel
                showLoading.postValue(false)
            }

            override fun fail(failData: Throwable) {
                navigator.showErrorMessage(getErrorMessage(failData))
                showLoading.postValue(false)
            }
        }, params))
    }

}