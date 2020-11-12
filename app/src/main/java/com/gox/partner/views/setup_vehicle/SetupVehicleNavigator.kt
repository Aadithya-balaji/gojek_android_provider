package com.gox.partner.views.setup_vehicle

interface SetupVehicleNavigator {
    fun onMenuItemClicked(position: Int)
    fun switchOnCliked(position: Int,status:Boolean)
    fun showError(error: String)
    fun showSuccess(status:Boolean,postion:Int)

}