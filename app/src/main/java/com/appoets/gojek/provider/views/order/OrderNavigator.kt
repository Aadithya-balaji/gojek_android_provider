package com.appoets.gojek.provider.views.order

import android.view.View

interface  OrderNavigator{
      fun getCurrentOrder()
      fun getPastOrder()
      fun setOrderType(view: View)
}