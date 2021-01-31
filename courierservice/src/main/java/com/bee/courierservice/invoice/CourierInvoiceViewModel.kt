package com.bee.courierservice.invoice

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.bee.courierservice.model.CourierCheckRequest
import com.bee.courierservice.model.UpdateRequest
import com.bee.courierservice.repositary.CourierRepository
import okhttp3.MediaType
import okhttp3.RequestBody
import com.bee.courierservice.model.PaymentModel
import com.bee.courierservice.model.ResponseData
import com.google.gson.Gson
import com.gox.base.base.BaseApplication
import com.gox.base.base.BaseViewModel
import com.gox.base.data.Constants
import com.gox.base.repository.ApiListener
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class CourierInvoiceViewModel : BaseViewModel<CourierInvoiceNavigator>() {

    private val mRepository = CourierRepository.instance()

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
    var checkStatusTaxiLiveData = MutableLiveData<CourierCheckRequest>()
    var invoiceUpdateRequest = MutableLiveData<CourierCheckRequest>()

    fun summit(){
        navigator.submit()
    }
    fun callXUberCheckRequest() {
        if (BaseApplication.isNetworkAvailable) {
            getCompositeDisposable().add(mRepository.xUberCheckRequest
            (object : ApiListener {
                override fun success(successData: Any) {
                    checkStatusTaxiLiveData.value = successData as CourierCheckRequest
                    showLoading.postValue(false)
                }

                override fun fail(failData: Throwable) {
                    navigator.showErrorMessage(getErrorMessage(failData))
                    showLoading.postValue(false)
                }
            }, "0.0", "0.0"))
        }
    }

    fun confirmPayment(pay_mode:String,id:Int) {
        if(pay_mode.equals(Constants.PaymentMode.CASH,true)){
            val params = HashMap<String, String>()
                showLoading.value = true
                params["id"] = id.toString()
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

    fun updatePayment(id:String,payment_by:String){
        val params = HashMap<String, RequestBody>()
        params[Constants.Common.ID] = id.toRequestBody("text/plain".toMediaTypeOrNull())
        if(payment_by.equals("SENDER",true))
        params[Constants.XUberProvider.STATUS] = "PAYMENT".toRequestBody("text/plain".toMediaTypeOrNull())
        else
            params[Constants.XUberProvider.STATUS] = "COMPLETED".toRequestBody("text/plain".toMediaTypeOrNull())

        params[Constants.Common.METHOD] = "PATCH".toRequestBody("text/plain".toMediaTypeOrNull())
        params[Constants.XUberProvider.EXTRA_CHARGE] = "".toRequestBody("text/plain".toMediaTypeOrNull())
        params[Constants.XUberProvider.EXTRA_CHARGE_NOTES] = "".toRequestBody("text/plain".toMediaTypeOrNull())
       getCompositeDisposable().add(mRepository.xUberUpdateRequest(object : ApiListener {
            override fun success(successData: Any) {
                invoiceUpdateRequest.value = successData as CourierCheckRequest
                showLoading.postValue(false)
            }
            override fun fail(failData: Throwable) {
                navigator.showErrorMessage(getErrorMessage(failData))
                showLoading.postValue(false)
            }
        }, params, null,null))
    }
}

