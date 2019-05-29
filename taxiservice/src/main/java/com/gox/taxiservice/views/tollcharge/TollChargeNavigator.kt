package com.gox.taxiservice.views.tollcharge

interface TollChargeNavigator {
    fun showErrorMessage(error: String)
    fun isValidCharge(): Boolean
}