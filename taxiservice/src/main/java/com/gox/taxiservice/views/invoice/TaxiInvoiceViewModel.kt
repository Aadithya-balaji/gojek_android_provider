package com.gox.taxiservice.views.invoice

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
import com.gox.taxiservice.model.PaymentModel
import com.gox.taxiservice.model.ResponseData
import com.gox.taxiservice.repositary.TaxiRepository

class TaxiInvoiceViewModel : BaseViewModel<TaxiInvoiceNavigator>() {

    private val mRepository = TaxiRepository.instance()

    var dropLocation = MutableLiveData<String>()
    var pickuplocation = MutableLiveData<String>()
    var bookingId = MutableLiveData<String>()
    var distance = MutableLiveData<String>()
    var timeTaken = MutableLiveData<String>()
    var baseFare = MutableLiveData<String>()
    var distanceFare = MutableLiveData<String>()
    var tax = MutableLiveData<String>()
    var waitingCharge = MutableLiveData<String>()
    var tips = MutableLiveData<String>()
    var tollCharge = MutableLiveData<String>()
    var total = MutableLiveData<String>()
    var requestLiveData = MutableLiveData<ResponseData>()
    var paymentLiveData = MutableLiveData<PaymentModel>()
    var showLoading = MutableLiveData<Boolean>()

    fun confirmPayment() {
        val params = HashMap<String, String>()
        if (requestLiveData.value != null && requestLiveData.value != null) {
            showLoading.value = true
            params["id"] = requestLiveData.value!!.request.id.toString()
            getCompositeDisposable().add(mRepository.confirmPayment(object : ApiListener {
                override fun success(successData: Any) {
                    paymentLiveData.postValue(successData as PaymentModel)
                    showLoading.postValue(false)
                }

                override fun fail(failData: Throwable) {
                    navigator.showErrorMessage(getErrorMessage(failData))
                    showLoading.postValue(false)
                }
            }, params))
        }
    }

    fun closeActivity() = navigator.closeInvoiceActivity()
}