package com.appoets.xjek.ui.currentorder_fragment

import com.appoets.basemodule.base.BaseViewModel

public class CurrentOrderViewModel : BaseViewModel<CurrentOrderNavigator>() {
    fun openDetailPage() {
        navigator.goToDetailPage()
    }
}