package com.gox.taxiservice.views.invoice

import com.gox.taxiservice.model.ResponseData

interface TaxiInvoiceNavigator {
    fun openRatingDialog(id: ResponseData?)
    fun tollCharge(amount: String)
    fun closeInvoiceActivity()
    fun showErrorMessage(error: String)
}
