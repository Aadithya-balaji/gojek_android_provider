package com.xjek.taxiservice.views.verifyotp

import com.xjek.base.base.BaseViewModel
import com.xjek.taxiservice.repositary.TaxiRepository

class VerifyOTPModule : BaseViewModel<VerifyOTPNavigator>() {

    fun proceedTrip() {
        navigator.startDrip()
    }

}
