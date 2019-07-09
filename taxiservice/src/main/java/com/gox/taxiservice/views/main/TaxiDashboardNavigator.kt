package com.gox.taxiservice.views.main

interface TaxiDashboardNavigator{

    fun showErrorMessage(error: String)
    fun updateCurrentLocation()
    fun showCurrentLocation()


}
