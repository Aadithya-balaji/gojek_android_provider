package com.gox.xuberservice.xuberMainActivity

import android.view.View

interface XUberDashBoardNavigator {
    fun showErrorMessage(error: String)
    fun updateService(view: View)
    fun showInfoWindow(view: View)
}
