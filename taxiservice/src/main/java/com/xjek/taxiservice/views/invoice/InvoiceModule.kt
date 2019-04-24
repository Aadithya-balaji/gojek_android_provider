package com.xjek.taxiservice.views.invoice

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.Constants
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.taxiservice.model.CheckRequestModel
import com.xjek.taxiservice.model.PaymentModel
import com.xjek.taxiservice.repositary.TaxiRepository

class InvoiceModule : BaseViewModel<InvoiceNavigator>() {
    val taxiRepository = TaxiRepository.instance()
    var bookingId = MutableLiveData<String>()
    var distance = MutableLiveData<String>()
    var timeTaken = MutableLiveData<String>()
    var baseFare = MutableLiveData<String>()
    var distanceFare = MutableLiveData<String>()
    var tax = MutableLiveData<String>()
    var tips = MutableLiveData<String>()
    var tollCharge = MutableLiveData<String>()
    var requestLiveData = MutableLiveData<CheckRequestModel>()
    var paymentLiveData = MutableLiveData<PaymentModel>()
    var showLoading = MutableLiveData<Boolean>()

    fun openRatingDialog() {
        navigator.openRatingDialog()
    }

    fun openTollDialog() {
        navigator.showTollDialog()
    }

    fun confirmPayment() {
        val params = HashMap<String, String>()
        if (requestLiveData.value != null && requestLiveData.value!!.responseData != null) {
            showLoading.value = true
            params.put(Constants.ConfirmPayment.ID, requestLiveData.value!!.responseData!!.request!!.id.toString())
            getCompositeDisposable().add(taxiRepository.confirmPayment(this, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), params))
        }
    }
}
