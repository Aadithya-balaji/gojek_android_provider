package com.appoets.taxiservice.views.views.verifyotp

import com.appoets.base.base.BaseViewModel

class VerifyOTPModule:BaseViewModel<VerifyOTPNavigator>(){
    fun  proceedTrip(){
        navigator.startDrip()
    }
}
