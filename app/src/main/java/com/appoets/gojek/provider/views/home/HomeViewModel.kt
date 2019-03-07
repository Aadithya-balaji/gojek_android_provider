package com.appoets.gojek.provider.views.home

import com.appoets.basemodule.base.BaseViewModel

class HomeViewModel :BaseViewModel<Home_Navigator>(){
    fun opentTranxitModule(){
        navigator.gotoTaxiModule()
    }

    fun openFoodieModule(){
        navigator.gotoFoodieModule()
    }

    fun openXuperMoudle(){
        navigator.gotoXuperModule()
    }
}
