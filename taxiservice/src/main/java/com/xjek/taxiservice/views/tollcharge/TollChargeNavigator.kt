package com.xjek.taxiservice.views.tollcharge

interface TollChargeNavigator {
    fun showErrorMessage(error: String)
    fun isValidCharge(): Boolean
}