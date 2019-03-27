package com.appoets.xjek.ui.signin

import androidx.databinding.Observable
import com.appoets.base.base.BaseViewModel

public class SigninViewModel : BaseViewModel<SigninNavigator>(),Observable
{
    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }


    fun  signinViaPhone()
    {
        navigator.changeSigninViaPhone()
    }

    fun signinMail()
    {
        navigator.changeSigninViaMail()

    }

    fun openSignUp()
    {
        navigator.goToSignup()
    }

    fun signIn()
    {
        //navigator.gotoHome()
    }

    fun goToHome(){
        navigator.gotoHome()
    }
}