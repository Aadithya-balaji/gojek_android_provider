package com.xjek.provider.views.countrylist

import com.xjek.base.base.BaseViewModel

class CountryViewModel:BaseViewModel<CountryNavigator>(){
    fun closeActivity(){
        navigator.closeActivity()
    }
}