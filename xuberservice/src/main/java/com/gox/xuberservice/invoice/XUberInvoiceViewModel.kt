package com.gox.xuberservice.invoice

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.data.Constants
import com.gox.base.repository.ApiListener
import com.gox.xuberservice.model.UpdateRequest
import com.gox.xuberservice.repositary.XUberRepository
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class XUberInvoiceViewModel : BaseViewModel<XuperInvoiceNavigator>() {

    private val mRepository = XUberRepository.instance()

    var userImage = MutableLiveData<String>()
    var userName = MutableLiveData<String>()
    var rating = MutableLiveData<String>()
    var serviceName = MutableLiveData<String>()
    var totalAmount = MutableLiveData<String>()
    var invoiceLiveData = MutableLiveData<UpdateRequest>()
    var requestID = MutableLiveData<String>()
    var paymentType:String =""
    var showLoading = MutableLiveData<Boolean>()
    var extraCharge = MutableLiveData<String>()
    var extraChargeNotes = MutableLiveData<String>()
    var tvAdditionalCharge = MutableLiveData<String>()

    fun confirmPayment() = navigator.submit()


    fun callConfirmPaymentApi() {
        if (paymentType.equals(Constants.PaymentMode.CASH, true)) {
            showLoading.value = true
            val params = HashMap<String, RequestBody>()
            params[Constants.Common.ID] = requestID.value.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            params[Constants.XUberProvider.STATUS] = "PAYMENT".toRequestBody("text/plain".toMediaTypeOrNull())
            params[Constants.Common.METHOD] = "PATCH".toRequestBody("text/plain".toMediaTypeOrNull())
            params["extra_charge_notes"] = extraChargeNotes.value.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            getCompositeDisposable().add(mRepository.confirmPayment(object : ApiListener {
                override fun success(successData: Any) {
                    invoiceLiveData.value = successData as UpdateRequest
                    showLoading.postValue(false)
                }

                override fun fail(failData: Throwable) {
                    navigator.showErrorMessage(getErrorMessage(failData))
                    showLoading.postValue(false)
                }
            }, params))
        } else {
                navigator.showRating()
        }

    }

}

