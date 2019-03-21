package com.appoets.gojek.taxi.views.views.bottomsheets

import com.appoets.basemodule.base.BaseViewModel

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
