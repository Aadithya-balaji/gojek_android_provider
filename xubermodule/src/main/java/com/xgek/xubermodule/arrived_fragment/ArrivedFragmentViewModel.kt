package com.xgek.xubermodule.arrived_fragment

import com.appoets.basemodule.base.BaseViewModel

class ArrivedFragmentViewModel : BaseViewModel<ArrivedFragmentNavigator>() {

    fun arrived() {
        navigator.goToArrivedState()
    }

    fun cancel() {
        navigator.cancelService()

    }
}