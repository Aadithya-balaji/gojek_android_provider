package com.appoets.gojek.provider.views.verifyfile

import com.appoets.basemodule.base.BaseViewModel

class VerifyFIleVIewModel:BaseViewModel<VerifyFileNavigator>(){
    fun showDashboardPage(){
        navigator.gotoDashBoardPage()
    }
}