package com.xgek.xubermodule.endservice_fragment

import com.appoets.basemodule.base.BaseViewModel

class EndServicesViewModel : BaseViewModel<EndServicesNavigator>() {


    fun endService() {
        navigator.endService()
    }
}
