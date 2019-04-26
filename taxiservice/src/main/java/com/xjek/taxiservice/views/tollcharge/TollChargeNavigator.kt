package com.xjek.taxiservice.views.tollcharge

interface TollChargeNavigator {
    fun addTollCharge()
    fun showErrorMessage(error: String)
    fun isValidCharge(): Boolean
}