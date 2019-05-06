package com.xjek.provider.views.countrypicker

import com.xjek.base.base.BaseViewModel
import com.xjek.gojek.taxiservice.views.views.countrypicker.CountrtCodeNavigator

class CountryCodeViewModel : BaseViewModel<CountrtCodeNavigator>() {

    fun closeActivity() {
        navigator.closeActivity()
    }

}