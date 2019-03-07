package com.appoets.gojek.traximodule.views.views.verifyotp

import com.appoets.basemodule.base.BaseViewModel

class VerifyOTPModule:BaseViewModel<VerifyOTPNavigator>(){
    fun  proceedTrip(){
        navigator.startDrip()
    }
}
