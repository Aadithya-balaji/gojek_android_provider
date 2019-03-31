package com.xjek.xjek.ui.currentorder_fragment

import com.xjek.base.base.BaseViewModel

public class CurrentOrderViewModel : BaseViewModel<CurrentOrderNavigator>() {
    fun openDetailPage() {
        navigator.goToDetailPage()
    }
}