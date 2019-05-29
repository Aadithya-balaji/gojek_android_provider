package com.gox.partner.views.add_vehicle

interface AddVehicleNavigator {
    fun onVehicleImageClicked()
    fun onRcBookClicked()
    fun onInsuranceClicked()
    fun onVehicleSubmitClicked()
    fun showError(error: String)
}