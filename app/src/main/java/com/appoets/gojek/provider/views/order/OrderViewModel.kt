package com.appoets.gojek.provider.views.order

import android.view.View
import com.appoets.basemodule.base.BaseViewModel

class  OrderViewModel :BaseViewModel<OrderNavigator>(){
    fun gotoPastOrder(){
        navigator.getPastOrder()
    }

    fun gotoCurrentOrder(){
        navigator.getCurrentOrder()
    }

    fun changeOrderType(view: View){
        navigator.setOrderType(view)
    }
}