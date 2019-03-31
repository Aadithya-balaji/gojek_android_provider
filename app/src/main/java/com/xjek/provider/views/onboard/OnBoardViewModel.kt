package com.xjek.provider.views.onboard

import com.xjek.base.base.BaseViewModel

class OnBoardViewModel (val onboardNavigator: OnBoardNavigator): BaseViewModel<OnBoardNavigator>() {

    fun openSignIn() {
        onboardNavigator.goToSignIn()
    }

    fun openSignUp() {
        onboardNavigator.goToSignUp()
    }

}
