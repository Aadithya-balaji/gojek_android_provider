package com.xjek.provider.views.dashboard

interface DashBoardNavigator {
    fun setTitle(title: String)
    fun hideLeftArrow(isTrue: Boolean)
    fun setLeftTitle(strTitle: String)
    fun showLogo(isNeedShow: Boolean)
    fun setRightIcon(rightIcon: Int)
    fun hideRightIcon(isNeedHide: Boolean)
    fun updateLocation(isTrue: Boolean)
    fun showErrorMessage(s: String)
    fun getInstance():DashBoardActivity
}

