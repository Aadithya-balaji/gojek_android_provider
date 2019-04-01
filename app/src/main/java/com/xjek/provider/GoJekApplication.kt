package com.xjek.provider

import com.xjek.base.base.BaseApplication
import com.xjek.base.utils.PreferenceHelper


class GoJekApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        PreferenceHelper(this)
    }
}