package com.gox.taxiservice.views.reasons

interface TaxiCancelReasonNavigator {
    fun closePopup()
    fun showErrorMessage(errorMessage: String)
}