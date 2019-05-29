package com.gox.partner.views.countrypicker

import com.gox.base.base.BaseViewModel
import com.gox.gojek.taxiservice.views.views.countrypicker.CountrtCodeNavigator

class CountryCodeViewModel : BaseViewModel<CountrtCodeNavigator>() {

    fun closeActivity() {
        navigator.closeActivity()
    }

}