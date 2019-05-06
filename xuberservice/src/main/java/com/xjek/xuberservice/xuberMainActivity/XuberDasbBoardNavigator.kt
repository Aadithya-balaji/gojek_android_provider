package com.xjek.xuberservice.xuberMainActivity

import android.view.View

interface XuberDasbBoardNavigator {

    fun goToLocationPick()
    fun goBack()
    fun showCurrentLocation()
    fun moveStatusFlow()
    fun showErrorMessage(error:String)
    fun showPicturePreview()
    fun updateService(view: View)
    fun showInfoWindow(view:View)
}
