package com.gox.partner.views.order

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel


class OrderFragmentViewModel : BaseViewModel<OrderFragmentNavigator>() {

     var selectedFilterService=MutableLiveData<String>()

    fun moveToCurrentOrderList() {
        navigator.goToCurrentOrder()

    }

    fun moveToPastOrderList() {
        navigator.goToPastOrder()
    }

    fun moveToUpcomingOrderList() {
        navigator.goToUpcomingOrder()
    }

    fun showfilter() {
        navigator.opeFilterlayout()
    }

    fun filterApplied() {

    }
}