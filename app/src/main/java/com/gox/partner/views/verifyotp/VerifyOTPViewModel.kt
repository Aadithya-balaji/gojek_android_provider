package com.gox.app.ui.verifyotp

import androidx.lifecycle.MutableLiveData
import com.gox.base.BuildConfig
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
import com.gox.partner.models.SendOTPResponse
import com.gox.partner.models.VerifyOTPResponse
import com.gox.partner.repository.AppRepository
import okhttp3.MediaType
import okhttp3.RequestBody

class VerifyOTPViewModel: BaseViewModel<VerifyOTPNavigator>(){
    var loadingProgress = MutableLiveData<Boolean>()
    var verifyOTPResponse = MutableLiveData<VerifyOTPResponse>()
    var errorResponse = MutableLiveData<String>()
    var countryCode = MutableLiveData<String>()
    var phoneNumber = MutableLiveData<String>()
    var sendOTPResponse = MutableLiveData<SendOTPResponse>()
    private val appRepository = AppRepository.instance()


    fun actionVerifyOTP(){
        navigator.verifyOTP()
    }


    fun verifyOTPApiCall(hashMap: HashMap<String, RequestBody>){
        getCompositeDisposable().add(appRepository.verifyOTP(this,hashMap))
    }

    fun resendOTP() {
        loadingProgress.value = true
        val hashMap: HashMap<String, RequestBody> = HashMap()
        hashMap.put("country_code", RequestBody.create(MediaType.parse("text/plain"), countryCode.value!!.replace("+", "")))
        hashMap.put("mobile", RequestBody.create(MediaType.parse("text/plain"), phoneNumber.value!!.toString()))
        hashMap.put("salt_key", RequestBody.create(MediaType.parse("text/plain"), BuildConfig.SALT_KEY))
        getCompositeDisposable().add(appRepository.sendOTP(object : ApiListener {
            override fun success(successData: Any) {
                sendOTPResponse.postValue(successData as SendOTPResponse)
                loadingProgress.value = false
            }

            override fun fail(error: Throwable) {
                errorResponse.postValue(getErrorMessage(error))
                loadingProgress.value = false
            }
        }, hashMap))
    }


}