package com.xjek.taxiservice.views.bottomsheets

import com.xjek.taxiservice.model.ResponseData

interface BottomSheetNavigator {
    fun whenStatusStarted(checkStatusModel: ResponseData?)
    fun openOTPDialog()
    fun startTrip(isTrue: Boolean)
    fun openInvoice()
    fun closeBottomSheet()
}