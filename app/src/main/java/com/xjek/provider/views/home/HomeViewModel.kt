package com.xjek.provider.views.home

import android.view.View
import com.xjek.base.base.BaseViewModel

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

    fun changeStatus(view: View){
        navigator.changeStatus(view)
    }
}
