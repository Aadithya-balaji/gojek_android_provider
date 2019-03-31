package com.xjek.foodservice.view

import com.xjek.base.base.BaseViewModel

class FoodLiveTaskServiceViewModel : BaseViewModel<FoodLiveTaskServiceNavigator>() {

    fun updateFoodDeliverStatus() {
        navigator.checkOrderDeliverStatus()
    }
}

