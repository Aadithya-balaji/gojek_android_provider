package com.xjek.taxiservice.views.invoice

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel

class InvoiceModule:BaseViewModel<InvoiceNavigator>(){
    var bookingId=MutableLiveData<String>()
    var distance=MutableLiveData<String>()
    var timeTaken=MutableLiveData<String>()
    var baseFare=MutableLiveData<String>()
    var distanceFare=MutableLiveData<String>()
    var tax=MutableLiveData<String>()
    var tips=MutableLiveData<String>()
    var tollCharge=MutableLiveData<String>()

    fun openRatingDialog(){
        navigator.openRatingDialog()
    }

    fun openTollDialog(){
        navigator.showTollDialog()
    }
}
