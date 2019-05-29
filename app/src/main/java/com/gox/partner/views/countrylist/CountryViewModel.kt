package com.gox.partner.views.countrylist

import com.gox.base.base.BaseViewModel

class CountryViewModel : BaseViewModel<CountryNavigator>() {
    fun closeActivity() {
        navigator.closeActivity()
    }
}