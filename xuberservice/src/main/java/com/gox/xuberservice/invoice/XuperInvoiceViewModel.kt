package com.gox.xuberservice.invoice

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.data.Constants
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.xuberservice.model.UpdateRequest
import com.gox.xuberservice.repositary.XuperRepoitory
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
    var extraCharge=MutableLiveData<String>()
    var extraChargeNotes=MutableLiveData<String>()

    fun confirmPayment() {
        navigator.submit()
    }

    fun showExtraCharegDialog(){
        navigator.showExtraChargePage()
    }

    fun callConfirmPaymentApi() {
        showProgress.value=true
        val params = HashMap<String, RequestBody>()
        params.put(Constants.Common.ID, RequestBody.create(MediaType.parse("text/plain"), requestID.value.toString()))
        params.put(Constants.XuperProvider.STATUS, RequestBody.create(MediaType.parse("text/plain"), "PAYMENT"))
        params.put(Constants.Common.METHOD, RequestBody.create(MediaType.parse("text/plain"), "PATCH"))
        params.put("extra_charge_notes", RequestBody.create(MediaType.parse("text/plain"),extraChargeNotes.value.toString()))
        getCompositeDisposable().add(xuperRepository.confirmPayment(this, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), params))
    }
}

