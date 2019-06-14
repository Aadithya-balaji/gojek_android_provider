package com.gox.partner.views.order

import com.gox.base.base.BaseViewModel

class OrderFragmentViewModel : BaseViewModel<OrderFragmentNavigator>() {

    fun moveToCurrentOrderList() = navigator.goToCurrentOrder()

    fun moveToUpcomingOrderList() = navigator.goToUpcomingOrder()

    fun showFilter() = navigator.opeFilterLayout()
}