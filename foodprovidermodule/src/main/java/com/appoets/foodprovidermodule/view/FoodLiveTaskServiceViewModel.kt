package com.appoets.foodprovidermodule.view

import com.appoets.basemodule.base.BaseViewModel

class FoodLiveTaskServiceViewModel : BaseViewModel<FoodLiveTaskServiceNavigator>() {

    fun updateFoodDeliverStatus() {
        navigator.checkOrderDeliverStatus()
    }
}

