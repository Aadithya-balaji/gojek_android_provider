package com.xjek.provider.views.home

import android.view.View

interface HomeNavigator {
    fun changeStatus(view: View)
    fun showErrorMessage(error: String)
    fun showCurrentLocation()
}
