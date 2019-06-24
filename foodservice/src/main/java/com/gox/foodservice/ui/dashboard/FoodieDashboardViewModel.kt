package com.gox.foodservice.ui.dashboard

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseApplication
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
import com.gox.foodservice.model.FoodieCheckRequestModel
import com.gox.foodservice.model.FoodieRatingRequestModel
import com.gox.foodservice.repositary.FoodieRepository.Companion.FoodieRepoInstance

class FoodieDashboardViewModel : BaseViewModel<FoodLiveTaskServiceNavigator>() {

    var foodieCheckRequestModel = MutableLiveData<FoodieCheckRequestModel>()
    var foodieUpdateRequestModel = MutableLiveData<FoodieCheckRequestModel>()
    var foodieRatingRequestModel = MutableLiveData<FoodieRatingRequestModel>()

    var showLoading = MutableLiveData<Boolean>()
    var latitude = MutableLiveData<Double>()
    var longitude = MutableLiveData<Double>()
    var orderId = MutableLiveData<Int>()

    fun updateFoodDeliverStatus() = navigator.checkOrderDeliverStatus()

    fun callFoodieCheckRequest() {
        if (BaseApplication.isNetworkAvailable)
            getCompositeDisposable().add(FoodieRepoInstance().foodieCheckRequest
            (object : ApiListener {
                override fun success(successData: Any) {
                    foodieCheckRequestModel.value = successData as FoodieCheckRequestModel
                    showLoading.postValue(false)
                }

                override fun fail(failData: Throwable) {
                    navigator.showErrorMessage(getErrorMessage(failData))
                    showLoading.postValue(false)
                }
            }))
    }

    fun callFoodieUpdateRequest(status: String) {
        showLoading.value = true
        val update: HashMap<String, String> = HashMap()
        update["id"] = orderId.value.toString()
        update["status"] = status
        update["_method"] = "PATCH"
        getCompositeDisposable().add(FoodieRepoInstance().foodieUpdateRequest(object : ApiListener {
            override fun success(successData: Any) {
                showLoading.postValue(false)
                foodieUpdateRequestModel.value = successData as FoodieCheckRequestModel
            }

            override fun fail(failData: Throwable) {
                showLoading.postValue(false)
                navigator.showErrorMessage(getErrorMessage(failData))
            }
        }, update))
    }

    fun callFoodieDeliveryRequest() {
        if (BaseApplication.isNetworkAvailable) {
            showLoading.value = true
            val update: HashMap<String, String> = HashMap()
            update["id"] = orderId.value.toString()
            update["status"] = "PAYMENT"
            update["_method"] = "PATCH"
            update["otp"] = foodieCheckRequestModel.value!!.responseData.requests.order_otp
            getCompositeDisposable().add(FoodieRepoInstance().foodieUpdateRequest(object : ApiListener {
                override fun success(successData: Any) {
                    showLoading.postValue(false)
                    foodieUpdateRequestModel.value = successData as FoodieCheckRequestModel
                }

                override fun fail(failData: Throwable) {
                    showLoading.postValue(false)
                    navigator.showErrorMessage(getErrorMessage(failData))
                }
            }, update))
        }
    }

    fun callFoodieRatingRequest(rating: String, comment: String) {
        if (BaseApplication.isNetworkAvailable) {
            showLoading.value = true
            val update: HashMap<String, String> = HashMap()
            update["id"] = orderId.value.toString()
            update["admin_service_id"] = foodieCheckRequestModel.value!!.responseData.requests.admin_service_id.toString()
            update["_method"] = "POST"
            update["rating"] = rating
            update["comment"] = comment
            getCompositeDisposable().add(FoodieRepoInstance().foodieRatingRequest
            (object : ApiListener {
                override fun success(successData: Any) {
                    showLoading.postValue(false)
                    foodieRatingRequestModel.value = successData as FoodieRatingRequestModel
                }

                override fun fail(failData: Throwable) {
                    showLoading.postValue(false)
                    navigator.showErrorMessage(getErrorMessage(failData))
                }
            }, update))
        }
    }
}
