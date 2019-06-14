package com.gox.partner.views.dashboard

interface DashBoardNavigator {
    fun setTitle(title: String)
    fun showLogo(isNeedShow: Boolean)
    fun setRightIcon(rightIcon: Int)
    fun hideRightIcon(isNeedHide: Boolean)
    fun updateLocation(isTrue: Boolean)
    fun showErrorMessage(s: String)
    fun getInstance(): DashBoardActivity
    fun updateCurrentLocation()
}

