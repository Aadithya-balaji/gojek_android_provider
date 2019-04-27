package com.xjek.taxiservice.views.invoice

import com.xjek.taxiservice.model.ResponseData

interface TaxiInvoiceNavigator{
    fun openRatingDialog(id: ResponseData?)
    fun tollCharge(amount:String)
    fun showErrorMessage(error:String)
}
