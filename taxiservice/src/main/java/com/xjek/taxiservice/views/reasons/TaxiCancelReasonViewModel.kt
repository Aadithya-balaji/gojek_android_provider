package com.xjek.taxiservice.views.reasons

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.Constants
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.taxiservice.model.ReasonModel
import com.xjek.taxiservice.repositary.TaxiRepository
import io.reactivex.disposables.Disposable

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