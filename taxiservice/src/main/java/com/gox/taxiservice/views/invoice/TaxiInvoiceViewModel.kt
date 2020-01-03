package com.gox.taxiservice.views.invoice

import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseApplication
import com.gox.base.base.BaseViewModel
import com.gox.base.data.Constants
import com.gox.base.repository.ApiListener
import com.gox.taxiservice.model.CheckRequestModel
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
    var discount = ObservableField<String>()
    var tax = MutableLiveData<String>()
    var waitingCharge = MutableLiveData<String>()
    var tips = MutableLiveData<String>()
    var tollCharge = MutableLiveData<String>()
    var payableAmount = ObservableField<String>()
    var total = MutableLiveData<String>()
    var requestLiveData = MutableLiveData<ResponseData>()
    var paymentLiveData = MutableLiveData<PaymentModel>()
    var showLoading = MutableLiveData<Boolean>()
    var checkStatusTaxiLiveData = MutableLiveData<CheckRequestModel>()


    fun confirmPayment() {
        val response = checkStatusTaxiLiveData.value
        if(response!!.responseData.request.payment.payment_mode.equals(Constants.PaymentMode.CASH,true)){
            val params = HashMap<String, String>()
            if (checkStatusTaxiLiveData.value != null) {
                showLoading.value = true
                params["id"] = checkStatusTaxiLiveData.value!!.responseData.request.id.toString()
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
        }else{
            navigator.openRatingDialog(response.responseData)
        }

    }

        fun callTaxiCheckStatusAPI() {
            if (BaseApplication.isNetworkAvailable)
                getCompositeDisposable().add(mRepository.checkRequest(object : ApiListener {
                    override fun success(successData: Any) {
                        checkStatusTaxiLiveData.value = successData as CheckRequestModel
                        showLoading.postValue(false)
                    }

                    override fun fail(failData: Throwable) {
                        navigator.showErrorMessage(getErrorMessage(failData))
                        showLoading.postValue(false)
                    }
                }))
        }

        fun closeActivity() = navigator.closeInvoiceActivity()
    }