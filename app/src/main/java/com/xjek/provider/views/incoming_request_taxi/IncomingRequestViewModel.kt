package com.xjek.provider.views.incoming_request_taxi

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel

class  IncomingRequestViewModel:BaseViewModel<IncomingNavigator>(){

    var pickupLocation=MutableLiveData<String>()
    var serviceType=MutableLiveData<String>()
    var timeLeft= MutableLiveData<String>()

    fun acceptReq(){
        navigator.accept()
    }

    fun cancelReq(){
        navigator.cancel()
    }

}