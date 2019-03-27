package com.appoets.taxiservice.views.verifyotp

import com.appoets.base.base.BaseViewModel

class VerifyOTPModule:BaseViewModel<VerifyOTPNavigator>(){
    fun  proceedTrip(){
        navigator.startDrip()
    }
}
