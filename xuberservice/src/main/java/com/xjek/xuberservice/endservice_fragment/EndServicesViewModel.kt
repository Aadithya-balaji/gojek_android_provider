package com.xjek.xuberservice.endservice_fragment

import com.appoets.base.base.BaseViewModel

class EndServicesViewModel : BaseViewModel<EndServicesNavigator>() {


    fun endService() {
        navigator.endService()
    }
}
