package com.xjek.provider.views.signin

import android.annotation.SuppressLint
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.provider.models.LoginResponseModel
import com.xjek.provider.network.WebApiConstants
import com.xjek.provider.repository.AppRepository

class SignInViewModel(val signInListener: SignInListener) : BaseViewModel<SignInViewModel.SignInListener>() {

    private val appRepository = AppRepository.instance()
    private var loginLiveData = MutableLiveData<LoginResponseModel>()


    val countryCode = MutableLiveData<String>()
    val phoneNumber = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()


    fun performSignInClick(view: View) {
        signInListener.performValidation()
    }

    @SuppressLint("CheckResult")
    fun postLogin() {
        val params = HashMap<String, String>()
        params[WebApiConstants.EMAIL] = email.value.toString()
        params[WebApiConstants.PASSWORD] = password.value.toString()
        params[WebApiConstants.SALT_KEY] = "MQ=="
        getCompositeDisposable().add(appRepository.postLogin(this, params))    }

    fun getLoginObservable() = loginLiveData
    fun getLoginResponseModel() = getLoginObservable().value



    interface SignInListener {
        fun performValidation()
        fun showError(error: String)

    }
}