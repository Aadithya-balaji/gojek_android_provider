package com.gox.partner.views.countrypicker

import com.gox.base.base.BaseViewModel

class CountryCodeViewModel : BaseViewModel<CountryCodeNavigator>() {

    fun closeActivity() {
        navigator.closeActivity()
    }

}