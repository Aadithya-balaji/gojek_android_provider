package com.xjek.xuberservice.endservice_fragment

import com.xjek.base.base.BaseViewModel

class EndServicesViewModel : BaseViewModel<EndServicesNavigator>() {


    fun endService() {
        navigator.endService()
    }
}
