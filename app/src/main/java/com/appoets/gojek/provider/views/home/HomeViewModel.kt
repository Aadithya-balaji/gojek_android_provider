package com.appoets.gojek.provider.views.home

import com.appoets.base.base.BaseViewModel

class HomeViewModel :BaseViewModel<Home_Navigator>(){
    fun opentTranxitModule(){
        navigator.gotoTaxiModule()
    }

    fun openFoodieModule(){
        navigator.gotoFoodieModule()
    }

    fun openXuberMoudle(){
        navigator.gotoXuberModule()
    }
}
