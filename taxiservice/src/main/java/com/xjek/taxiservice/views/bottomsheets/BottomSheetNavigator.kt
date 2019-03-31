package com.xjek.taxiservice.views.bottomsheets

interface BottomSheetNavigator{
    fun  openOTPDialog()
    fun  startTrip(isTrue:Boolean)
    fun  opentInvoice()
    fun  closeBottomSheet()
}