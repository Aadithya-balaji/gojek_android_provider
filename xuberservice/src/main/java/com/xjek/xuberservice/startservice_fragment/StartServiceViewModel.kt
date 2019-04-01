package com.xjek.xuberservice.startservice_fragment

import com.xjek.base.base.BaseViewModel

class StartServiceViewModel : BaseViewModel<StartServiceNavigator>() {

    fun startService() {
        navigator.startService()
    }
}