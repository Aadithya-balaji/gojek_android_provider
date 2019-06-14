package com.gox.partner.views.support

import com.gox.base.base.BaseViewModel

public class SupportViewModel : BaseViewModel<SupportNavigator>() {
    fun makeCall() {
        navigator.goToPhoneCall()
    }

    fun sendMail() {
        navigator.goToMail()
    }

    fun openWebsite() {
        navigator.goToWebsite()

    }
}