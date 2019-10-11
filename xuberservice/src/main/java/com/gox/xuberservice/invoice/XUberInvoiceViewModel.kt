package com.gox.xuberservice.invoice

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.data.Constants
import com.gox.base.repository.ApiListener
import com.gox.xuberservice.model.UpdateRequest
import com.gox.xuberservice.repositary.XUberRepository
import okhttp3.MediaType
import okhttp3.RequestBody

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
            params[Constants.Common.ID] = RequestBody.create(MediaType.parse("text/plain"), requestID.value.toString())
            params[Constants.XUberProvider.STATUS] = RequestBody.create(MediaType.parse("text/plain"), "PAYMENT")
            params[Constants.Common.METHOD] = RequestBody.create(MediaType.parse("text/plain"), "PATCH")
            params["extra_charge_notes"] = RequestBody.create(MediaType.parse("text/plain"), extraChargeNotes.value.toString())
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

