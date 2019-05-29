package com.gox.foodservice.ui.dashboard

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.foodservice.model.FoodieCheckRequestModel
import com.gox.foodservice.model.FoodieRatingRequestModel
import com.gox.foodservice.repositary.FoodieRepository

class FoodLiveTaskServiceViewModel : BaseViewModel<FoodLiveTaskServiceNavigator>() {

    var latitude = MutableLiveData<Double>()
    var longitude = MutableLiveData<Double>()
    var foodieCheckRequestModel = MutableLiveData<FoodieCheckRequestModel>()
    var showLoading = MutableLiveData<Boolean>()
    var foodieRatingRequestModel = MutableLiveData<FoodieRatingRequestModel>()

    fun updateFoodDeliverStatus() {
        navigator.checkOrderDeliverStatus()
    }

    fun callFoodieCheckRequest() {
        getCompositeDisposable().add(FoodieRepository.instance().foodieCheckRequest
        (this, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN)))
    }

    fun callFoodieUpdateRequest(status: String) {
        showLoading.value = true
        val update: HashMap<String, String> = HashMap()
        update["id"] = foodieCheckRequestModel.value?.responseData?.requests?.id.toString()
        update["status"] = status
        update["_method"] = "PATCH"
        getCompositeDisposable().add(FoodieRepository.instance().foodieUpdateRequest
        (this, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), update))
    }

    fun callFoodieDeliveryRequest() {
        showLoading.value = true
        val update: HashMap<String, String> = HashMap()
        update["id"] = foodieCheckRequestModel.value!!.responseData.requests.id
        update["status"] = "PAYMENT"
        update["_method"] = "PATCH"
        update["otp"] = foodieCheckRequestModel.value!!.responseData.requests.order_otp
        getCompositeDisposable().add(FoodieRepository.instance().foodieUpdateRequest
        (this, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), update))
    }

    fun callFoodieRatingRequest(rating: String, comment: String) {
        showLoading.value = true
        val update: HashMap<String, String> = HashMap()
        update["id"] = foodieCheckRequestModel.value!!.responseData.requests.id
        update["admin_service_id"] = foodieCheckRequestModel.value!!.responseData.requests.admin_service_id
        update["_method"] = "POST"
        update["rating"] = rating
        update["comment"] = comment
        getCompositeDisposable().add(FoodieRepository.instance().foodieRatingRequest
        (this, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), update))
    }
}
