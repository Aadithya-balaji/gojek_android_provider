package com.xjek.xuberservice.startservice_fragment

import com.appoets.base.base.BaseViewModel

class StartServiceViewModel : BaseViewModel<StartServiceNavigator>() {

    fun startService() {
        navigator.startService()
    }
}