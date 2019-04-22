package com.xjek.taxiservice.views.bottomsheets

import com.xjek.base.base.BaseViewModel

class RideStatusViewModel : BaseViewModel<BottomSheetNavigator>() {

    fun onArrivedStatus(){
        navigator.whenArrivedStatus()
    }

    fun openOTPDialogFrag() {
        navigator.openOTPDialog()
    }

    fun gotoInvoicePage() {
        navigator.openInvoice()
    }

    fun closeDialog() {
        navigator.closeBottomSheet()
    }

}
