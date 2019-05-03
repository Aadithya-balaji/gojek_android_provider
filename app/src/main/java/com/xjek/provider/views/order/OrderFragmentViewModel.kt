package com.xjek.provider.views.order

import com.xjek.base.base.BaseViewModel


class OrderFragmentViewModel : BaseViewModel<OrderFragmentNavigator>() {

     lateinit var selectedFilterService :String

    fun moveToCurrentOrderList() {
        navigator.goToCurrentOrder()

    }

    fun moveToPastOrderList() {
        navigator.goToPastOrder()
    }

    fun moveToUpcomingOrderList() {
        navigator.goToUpcomingOrder()
    }

    fun showfilter()
    {
        navigator.opeFilterlayout()
    }

    fun filterApplied()
    {

    }
}