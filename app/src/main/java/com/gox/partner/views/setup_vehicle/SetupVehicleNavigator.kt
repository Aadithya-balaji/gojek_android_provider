package com.gox.partner.views.setup_vehicle

interface SetupVehicleNavigator {
    fun onMenuItemClicked(position: Int)
    fun showError(error: String)
}