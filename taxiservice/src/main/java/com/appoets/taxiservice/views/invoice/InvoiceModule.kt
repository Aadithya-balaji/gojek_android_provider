package com.appoets.taxiservice.views.invoice

import com.appoets.base.base.BaseViewModel

class InvoiceModule:BaseViewModel<InvoiceNavigator>(){
    fun openRatingDialog(){
        navigator.openRatingDialog()
    }
}
