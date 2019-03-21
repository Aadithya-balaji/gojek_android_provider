package com.appoets.gojek.taxi.views.views.bottomsheets

interface BottomSheetNavigator{
    fun  openOTPDialog()
    fun  startTrip(isTrue:Boolean)
    fun  opentInvoice()
    fun  closeBottomSheet()
}