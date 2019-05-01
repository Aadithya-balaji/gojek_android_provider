package com.xjek.provider.views.manage_services

interface ManageServicesNavigator {
    fun onMenuItemClicked(position: Int)
    fun showError(error: String)
}