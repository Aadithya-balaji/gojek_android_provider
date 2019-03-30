package com.appoets.gojek.provider

import com.appoets.base.base.BaseApplication
import com.appoets.base.utils.PreferenceHelper

class GoJekApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        PreferenceHelper(this)
    }
}