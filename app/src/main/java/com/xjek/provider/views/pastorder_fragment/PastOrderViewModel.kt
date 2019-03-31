package com.xjek.xjek.ui.pastorder_fragment

import com.xjek.base.base.BaseViewModel

public class PastOrderViewModel : BaseViewModel<PastOrderNavigator>() {
    fun openDetailPage() {
        navigator.gotoDetailPage()

    }
}