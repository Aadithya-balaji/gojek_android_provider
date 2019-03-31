package com.xjek.taxiservice.views.bottomsheets

import com.xjek.base.base.BaseViewModel

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
