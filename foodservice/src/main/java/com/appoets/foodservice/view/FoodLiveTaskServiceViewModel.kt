package com.appoets.foodservice.view

import com.appoets.base.base.BaseViewModel

class FoodLiveTaskServiceViewModel : BaseViewModel<FoodLiveTaskServiceNavigator>() {

    fun updateFoodDeliverStatus() {
        navigator.checkOrderDeliverStatus()
    }
}

