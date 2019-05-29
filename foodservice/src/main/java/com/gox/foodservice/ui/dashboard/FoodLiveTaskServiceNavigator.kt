package com.gox.foodservice.ui.dashboard

interface FoodLiveTaskServiceNavigator {
    fun checkOrderDeliverStatus()
    fun showErrorMessage(error: String)
}
