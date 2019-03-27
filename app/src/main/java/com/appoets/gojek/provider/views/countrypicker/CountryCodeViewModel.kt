package com.appoets.gojek.provider.views.countrypicker

import com.appoets.basemodule.base.BaseViewModel
import com.appoets.gojek.traximodule.views.views.countrypicker.CountrtCodeNavigator

class  CountryCodeViewModel:BaseViewModel<CountrtCodeNavigator>(){

    fun closeActivity(){
        navigator.closeActivity()
    }

}