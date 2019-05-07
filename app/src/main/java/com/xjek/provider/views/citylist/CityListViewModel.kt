package com.xjek.provider.views.citylist

import com.xjek.base.base.BaseViewModel

class CityListViewModel : BaseViewModel<CityListNavigator>() {
    fun closeActivity() {
        navigator.closeActivity()
    }
}