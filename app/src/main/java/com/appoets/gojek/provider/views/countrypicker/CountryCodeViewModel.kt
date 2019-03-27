package com.appoets.gojek.provider.views.countrypicker

import com.appoets.base.base.BaseViewModel
import com.appoets.gojek.taxiservice.views.views.countrypicker.CountrtCodeNavigator

class  CountryCodeViewModel:BaseViewModel<CountrtCodeNavigator>(){

    fun closeActivity(){
        navigator.closeActivity()
    }

}