package com.xjek.foodservice.view

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.foodservice.view.model.FoodieCheckRequestModel
import com.xjek.foodservice.view.repositary.FoodieRepository

class FoodLiveTaskServiceViewModel : BaseViewModel<FoodLiveTaskServiceNavigator>() {

    var latitude = MutableLiveData<Double>()
    var longitude = MutableLiveData<Double>()
    var foodieCheckRequestModel = MutableLiveData<FoodieCheckRequestModel>()

    fun updateFoodDeliverStatus() {
        navigator.checkOrderDeliverStatus()
    }

    fun callFoodieCheckRequest() {
        getCompositeDisposable().add(FoodieRepository.instance().foodieCheckRequest
        (this, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), latitude.value.toString(), longitude.value.toString()))
    }
}
