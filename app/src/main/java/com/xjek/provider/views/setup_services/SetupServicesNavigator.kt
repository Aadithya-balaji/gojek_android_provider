package com.xjek.provider.views.setup_services

interface SetupServicesNavigator {
    fun onMenuItemClicked(position: Int)
    fun showError(error: String)
}