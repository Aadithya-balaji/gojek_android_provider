package com.appoets.gojek.provider.views.signin

import android.annotation.SuppressLint
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.appoets.gojek.provider.models.LoginResponseModel
import com.appoets.gojek.provider.network.WebApiConstants
import com.appoets.gojek.provider.repository.AppRepository

class SignInViewModel(val signInListener: SignInListener) : ViewModel() {

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
        appRepository.postLogin(this, params)
    }

    fun getLoginObservable() = loginLiveData

    fun getLoginResponseModel() = getLoginObservable().value

    interface SignInListener {
        fun performValidation()
        fun showError(error: String)
    }
}