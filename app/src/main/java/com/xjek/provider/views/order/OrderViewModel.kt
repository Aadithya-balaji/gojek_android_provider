package com.xjek.provider.views.order

import com.xjek.base.base.BaseViewModel

class  OrderViewModel :BaseViewModel<OrderNavigator>(){
    fun gotoPastOrder(){
        navigator.getPastOrder()
    }

    fun gotoCurrentOrder(){
        navigator.getCurrentOrder()
    }

}