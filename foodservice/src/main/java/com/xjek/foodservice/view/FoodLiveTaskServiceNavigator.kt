package com.xjek.foodservice.view

interface FoodLiveTaskServiceNavigator {
    fun checkOrderDeliverStatus()
    fun showErrorMessage(error: String)
}
