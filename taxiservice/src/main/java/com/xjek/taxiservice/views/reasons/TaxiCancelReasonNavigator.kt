package com.xjek.taxiservice.views.reasons

interface TaxiCancelReasonNavigator {
    fun closePopup()
    fun showErrorMessage(errorMessage: String)
}