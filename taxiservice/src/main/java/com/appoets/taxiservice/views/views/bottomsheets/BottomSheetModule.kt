package com.appoets.taxiservice.views.views.bottomsheets

import com.appoets.base.base.BaseViewModel

class BottomSheetModule:BaseViewModel<BottomSheetNavigator>(){
    fun openOTPDialogFrag(){
        navigator.openOTPDialog()
    }

    fun gotoInvoicePage(){
         navigator.opentInvoice()
    }

    fun closeDialog(){
        navigator.closeBottomSheet()
    }

}
