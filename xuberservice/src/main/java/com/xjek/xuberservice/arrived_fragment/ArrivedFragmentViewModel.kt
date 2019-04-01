package com.xjek.xuberservice.arrived_fragment

import com.xjek.base.base.BaseViewModel

class ArrivedFragmentViewModel : BaseViewModel<ArrivedFragmentNavigator>() {

    fun arrived() {
        navigator.goToArrivedState()
    }

    fun cancel() {
        navigator.cancelService()

    }
}