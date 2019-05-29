package com.gox.taxiservice.views.reasons

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.taxiservice.model.ReasonModel
import com.gox.taxiservice.repositary.TaxiRepository

class TaxiCancelReasonViewModel : BaseViewModel<TaxiCancelReasonNavigator>() {

    private val mRepository = TaxiRepository.instance()
    val mResponse = MutableLiveData<ReasonModel>()
    var errorResponse = MutableLiveData<Throwable>()

    fun dismissPopup() {
        navigator.closePopup()
    }

    fun getReason() {
        getCompositeDisposable().add(mRepository.taxiGetReason(this,
                "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN)))
    }
}