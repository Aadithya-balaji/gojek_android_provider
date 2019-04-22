package com.xjek.taxiservice.views.bottomsheets

import com.xjek.taxiservice.model.ResponseData

interface BottomSheetNavigator {
    fun whenStatusStarted(checkStatusModel: ResponseData?)
    fun whenArrivedStatus()
    fun openOTPDialog()
    fun whenStatusArrived(isTrue: ResponseData?)
    fun openInvoice()
    fun closeBottomSheet()
}