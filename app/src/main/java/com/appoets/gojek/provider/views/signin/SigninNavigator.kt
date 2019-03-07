package com.appoets.xjek.ui.signin

public interface SigninNavigator {

    fun changeSigninViaPhone(): Boolean
    fun changeSigninViaMail(): Boolean
    fun goToSignup()
    fun googleSignin()
    fun facebookSignin()
    fun gotoHome()

}