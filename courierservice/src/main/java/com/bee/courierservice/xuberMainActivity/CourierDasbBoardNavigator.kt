package com.bee.courierservice.xuberMainActivity

import android.view.View

interface CourierDashBoardNavigator {
    fun showErrorMessage(error: String)
    fun updateService(view: View)
    fun showInfoWindow(view: View)
}
