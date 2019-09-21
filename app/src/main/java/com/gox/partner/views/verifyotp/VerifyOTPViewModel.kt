package com.gox.app.ui.verifyotp

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.partner.models.VerifyOTPResponse
import com.gox.partner.repository.AppRepository
import okhttp3.RequestBody

class VerifyOTPViewModel: BaseViewModel<VerifyOTPNavigator>(){
    var loadingProgress = MutableLiveData<Boolean>()
    var verifyOTPResponse = MutableLiveData<VerifyOTPResponse>()
    var errorResponse = MutableLiveData<String>()

    private val appRepository = AppRepository.instance()


    fun actionVerifyOTP(){
        navigator.verifyOTP()
    }


    fun verifyOTPApiCall(hashMap: HashMap<String, RequestBody>){
        getCompositeDisposable().add(appRepository.verifyOTP(this,hashMap))
    }



}