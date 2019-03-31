package com.xjek.taxiservice.views.invoice

import com.xjek.base.base.BaseViewModel

class InvoiceModule:BaseViewModel<InvoiceNavigator>(){
    fun openRatingDialog(){
        navigator.openRatingDialog()
    }
}
