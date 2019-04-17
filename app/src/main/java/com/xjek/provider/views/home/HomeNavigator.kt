package com.xjek.provider.views.home

import android.view.View

interface HomeNavigator {
    fun gotoTaxiModule()
    fun gotoFoodieModule()
    fun gotoXuberModule()
    fun changeStatus(view: View)
    fun showErrormessage(error:String)
}
