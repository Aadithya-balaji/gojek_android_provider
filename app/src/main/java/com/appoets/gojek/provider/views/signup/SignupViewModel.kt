package com.appoets.xjek.ui.signup

import com.appoets.base.base.BaseViewModel

class SignupViewModel : BaseViewModel<SignupNavigator>() {

    fun doRegistration() {
        navigator.signup()
    }

    fun gotoSignin() {
        navigator.openSignin()
    }

    fun gotoDocumentPage(){
        navigator.gotoDocumentPage()
    }
}