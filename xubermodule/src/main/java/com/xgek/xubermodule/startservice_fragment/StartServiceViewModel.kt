package com.xgek.xubermodule.startservice_fragment

import com.appoets.basemodule.base.BaseViewModel

class StartServiceViewModel : BaseViewModel<StartServiceNavigator>()
{

    fun startService()
    {
        navigator.startService()
    }
}