package com.gox.taxiservice.views.rating

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.taxiservice.model.TaxiRatingResponse
import com.gox.taxiservice.repositary.TaxiRepository

class TaxiRatingViewModel : BaseViewModel<TaxiRatingNavigator>() {

    private val mRepository = TaxiRepository.instance()
    var ratingLiveData = MutableLiveData<TaxiRatingResponse>()
    var showLoading = MutableLiveData<Boolean>()

    fun submitRating(params: HashMap<String, String>) {
        getCompositeDisposable().add(mRepository.submitRating
        (this, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), params))
    }
}