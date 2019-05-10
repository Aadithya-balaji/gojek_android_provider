package com.xjek.xuberservice.xuberMainActivity

import android.view.View

interface XuberDasbBoardNavigator {

    fun goToLocationPick()
    fun goBack()
    fun moveStatusFlow()
    fun showErrorMessage(error: String)
    fun showPicturePreview(isFrontImage: Boolean)
    fun updateService(view: View)
    fun showInfoWindow(view: View)
}
