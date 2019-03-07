package com.appoets.gojek.traximodule.views.views.invoice

import com.appoets.basemodule.base.BaseViewModel

class InvoiceModule:BaseViewModel<InvoiceNavigator>(){
    fun openRatingDialog(){
        navigator.openRatingDialog()
    }
}
