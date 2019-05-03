package com.xjek.xuberservice.invoice

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.Constants
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.xuberservice.model.UpdateRequest
import com.xjek.xuberservice.repositary.XuperRepoitory
import okhttp3.MediaType
import okhttp3.RequestBody

class XuperInvoiceViewModel : BaseViewModel<XuperInvoiceNavigator>() {
    val xuperRepository = XuperRepoitory.instance()
    var userImage = MutableLiveData<String>()
    var userName=MutableLiveData<String>()
    var rating = MutableLiveData<String>()
    var serviceName = MutableLiveData<String>()
    var timeTaken = MutableLiveData<String>()
    var totalAmount = MutableLiveData<String>()
    var invoiceLiveData = MutableLiveData<UpdateRequest>()
    var requestID = MutableLiveData<String>()
    var showProgress = MutableLiveData<Boolean>()

    fun confirmPayment() {
        navigator.submit()
    }

    fun callConfirmPaymentApi() {
        val params = HashMap<String, RequestBody>()
        params.put(Constants.Common.ID, RequestBody.create(MediaType.parse("text/plain"), requestID.value))
        params.put(Constants.XuperProvider.STATUS, RequestBody.create(MediaType.parse("text/plain"), "PAYMENT"))
        params.put(Constants.Common.METHOD, RequestBody.create(MediaType.parse("text/plain"), "PATCH"))
        getCompositeDisposable().add(xuperRepository.confirmPayment(this, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), params, null))
    }
}

