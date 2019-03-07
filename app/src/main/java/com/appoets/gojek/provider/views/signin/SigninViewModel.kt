package com.appoets.xjek.ui.signin

import androidx.databinding.BaseObservable
import androidx.databinding.Observable
import com.appoets.basemodule.base.BaseViewModel

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
        navigator.gotoHome()
    }
}