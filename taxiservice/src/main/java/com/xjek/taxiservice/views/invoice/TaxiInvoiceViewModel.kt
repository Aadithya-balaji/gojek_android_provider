package com.xjek.taxiservice.views.invoice

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.taxiservice.model.PaymentModel
import com.xjek.taxiservice.model.ResponseData
import com.xjek.taxiservice.repositary.TaxiRepository

class TaxiInvoiceViewModel : BaseViewModel<TaxiInvoiceNavigator>() {

    private val taxiRepository = TaxiRepository.instance()

    var dropLocation = MutableLiveData<String>()
    var pickuplocation = MutableLiveData<String>()
    var bookingId = MutableLiveData<String>()
    var distance = MutableLiveData<String>()
    var timeTaken = MutableLiveData<String>()
    var baseFare = MutableLiveData<String>()
    var distanceFare = MutableLiveData<String>()
    var tax = MutableLiveData<String>()
    var tips = MutableLiveData<String>()
    var tollCharge = MutableLiveData<String>()
    var requestLiveData = MutableLiveData<ResponseData>()
    var paymentLiveData = MutableLiveData<PaymentModel>()
    var showLoading = MutableLiveData<Boolean>()

    fun openTollDialog() {
        navigator.showTollDialog()
    }

    fun confirmPayment() {
        val params = HashMap<String, String>()
        if (requestLiveData.value != null && requestLiveData.value != null) {
            showLoading.value = true
            params["id"] = requestLiveData.value!!.request.id.toString()
            getCompositeDisposable().add(taxiRepository.confirmPayment
            (this, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), params))
        }
    }
}