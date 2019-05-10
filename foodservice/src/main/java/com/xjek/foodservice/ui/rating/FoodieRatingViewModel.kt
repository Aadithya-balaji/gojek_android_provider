package com.xjek.foodservice.ui.rating

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.foodservice.model.FoodieCheckRequestModel
import com.xjek.foodservice.repositary.FoodieRepository

class FoodieRatingViewModel : BaseViewModel<FoodieRatingNavigator>() {

    private val mRepository = FoodieRepository.instance()
    var ratingLiveData = MutableLiveData<FoodieCheckRequestModel>()
    var showLoading = MutableLiveData<Boolean>()

    fun submitRating(params: HashMap<String, String>) {
//        getCompositeDisposable().add(mRepository.foodieCheckRequest
//        (this, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), params))
    }
}
