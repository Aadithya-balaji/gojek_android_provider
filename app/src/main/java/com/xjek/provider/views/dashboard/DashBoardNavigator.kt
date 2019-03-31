package com.xjek.provider.views.dashboard

import android.os.Bundle

interface DashBoardNavigator {
    fun switchFragment(fragmentName: String, bundle: Bundle, isNeedAnimation: Boolean, isClearBackStack: Boolean)
    fun getInstance(): DashBoardActivity
    fun setTitle(title: String)
    fun hideLeftArrow(isTrue:Boolean)
    fun setLeftTitle(strTitle: String)
    fun showLogo(isNeedShow:Boolean)
    fun setRightIcon(rightIcon:Int)
    fun  hideRightIcon(isNeedHide:Boolean)
}

