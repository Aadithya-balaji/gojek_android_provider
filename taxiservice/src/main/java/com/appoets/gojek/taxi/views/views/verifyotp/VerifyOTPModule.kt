package com.appoets.gojek.taxi.views.views.verifyotp

import com.appoets.basemodule.base.BaseViewModel

class VerifyOTPModule:BaseViewModel<VerifyOTPNavigator>(){
    fun  proceedTrip(){
        navigator.startDrip()
    }
}
