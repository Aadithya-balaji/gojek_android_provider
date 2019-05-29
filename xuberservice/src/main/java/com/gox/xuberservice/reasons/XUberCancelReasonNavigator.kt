package com.gox.xuberservice.reasons

interface XUberCancelReasonNavigator {
    fun closePopup()
    fun getErrorMessage(error:String)
}