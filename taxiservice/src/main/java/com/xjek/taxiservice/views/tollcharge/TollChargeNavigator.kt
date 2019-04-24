package com.xjek.taxiservice.views.tollcharge

interface  TollChargeNavigator {
    fun addTollCharge()
    fun  dismissDialog()
    fun showErrorMessage(error:String)
    fun  isValidCharge():Boolean
}