package com.gox.foodservice.ui.dashboard

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseApplication
import com.gox.base.base.BaseViewModel
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.foodservice.model.FoodieCheckRequestModel
import com.gox.foodservice.model.FoodieRatingRequestModel
import com.gox.foodservice.repositary.FoodieRepository

class FoodieDashboardViewModel : BaseViewModel<FoodLiveTaskServiceNavigator>() {

    var latitude = MutableLiveData<Double>()
    var longitude = MutableLiveData<Double>()
    var foodieCheckRequestModel = MutableLiveData<FoodieCheckRequestModel>()
    var foodieUpdateRequestModel = MutableLiveData<FoodieCheckRequestModel>()
    var showLoading = MutableLiveData<Boolean>()
    var foodieRatingRequestModel = MutableLiveData<FoodieRatingRequestModel>()
    var orderId = MutableLiveData<Int>()

    fun updateFoodDeliverStatus() {
        navigator.checkOrderDeliverStatus()
    }

    fun callFoodieCheckRequest() {
        if (BaseApplication.isNetworkAvailable)
            getCompositeDisposable().add(FoodieRepository.instance().foodieCheckRequest
            (this, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN)))
    }

    fun callFoodieUpdateRequest(status: String) {
        showLoading.value = true
        val update: HashMap<String, String> = HashMap()
        update["id"] = orderId.value.toString()
        update["status"] = status
        update["_method"] = "PATCH"
        getCompositeDisposable().add(FoodieRepository.instance().foodieUpdateRequest
        (this, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), update))
    }

    fun callFoodieDeliveryRequest() {
        if (BaseApplication.isNetworkAvailable) {
            showLoading.value = true
            val update: HashMap<String, String> = HashMap()
            update["id"] = orderId.value.toString()
            update["status"] = "PAYMENT"
            update["_method"] = "PATCH"
            update["otp"] = foodieCheckRequestModel.value!!.responseData.requests.order_otp
            getCompositeDisposable().add(FoodieRepository.instance().foodieUpdateRequest
            (this, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), update))
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
            getCompositeDisposable().add(FoodieRepository.instance().foodieRatingRequest
            (this, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), update))
        }
    }
}
