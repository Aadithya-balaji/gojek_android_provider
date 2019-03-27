package com.appoets.gojek.provider.views.order

import com.appoets.base.base.BaseViewModel

class  OrderViewModel :BaseViewModel<OrderNavigator>(){
    fun gotoPastOrder(){
        navigator.getPastOrder()
    }

    fun gotoCurrentOrder(){
        navigator.getCurrentOrder()
    }

}