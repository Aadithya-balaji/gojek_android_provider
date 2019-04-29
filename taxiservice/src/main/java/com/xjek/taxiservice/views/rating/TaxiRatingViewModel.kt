package com.xjek.taxiservice.views.rating

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.taxiservice.model.TaxiRatingResponse
import com.xjek.taxiservice.repositary.TaxiRepository

class TaxiRatingViewModel : BaseViewModel<TaxiRatingNavigator>() {

    private val mRepository = TaxiRepository.instance()
    var ratingLiveData = MutableLiveData<TaxiRatingResponse>()
    var showLoading = MutableLiveData<Boolean>()

    fun submitRating(params: HashMap<String, String>) {
        getCompositeDisposable().add(mRepository.submitRating
        (this, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), params))
    }
}