package com.appoets.xjek.ui.pastorder_fragment

import com.appoets.basemodule.base.BaseViewModel

public class PastOrderViewModel : BaseViewModel<PastOrderNavigator>() {
    fun openDetailPage() {
        navigator.gotoDetailPage()

    }
}