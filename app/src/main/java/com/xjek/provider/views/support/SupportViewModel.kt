package com.xjek.xjek.ui.support

import com.xjek.base.base.BaseViewModel

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