package com.xjek.taxiservice.views.invoice

interface InvoiceNavigator{
    fun openRatingDialog()
    fun showTollDialog()
    fun tollCharge(amount:String)
}
