package com.gox.partner.views.citylist

import com.gox.base.base.BaseViewModel

class CityListViewModel : BaseViewModel<CityListNavigator>() {
    fun closeActivity() {
        navigator.closeActivity()
    }
}