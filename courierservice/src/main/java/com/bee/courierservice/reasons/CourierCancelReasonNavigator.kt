package com.bee.courierservice.reasons

interface CourierCancelReasonNavigator {
    fun closePopup()
    fun getErrorMessage(error:String)
}