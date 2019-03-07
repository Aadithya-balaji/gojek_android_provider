package com.appoets.gojek.provider.views.dashboard

import android.os.Bundle

interface DashBoardNavigator {
    fun switchFragment(fragmentName: String, bundle: Bundle, isNeedAnimation: Boolean, isClearBackStack: Boolean)
    fun getInstance(): DashBoardActivity
    fun setTitle(title: String)
    fun hideLeftArrow(isTrue:Boolean)
}

