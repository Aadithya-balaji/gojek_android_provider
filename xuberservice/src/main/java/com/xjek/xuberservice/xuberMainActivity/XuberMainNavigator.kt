package com.xjek.xuberservice.xuberMainActivity

import android.view.View

interface XuberMainNavigator {

    fun goToLocationPick()
    fun goBack()
    fun showCurrentLocation()
    fun moveStatusFlow()
    fun showErrorMessage(error:String)
    fun showPicturePreview()
    fun updateService(view: View)
}
