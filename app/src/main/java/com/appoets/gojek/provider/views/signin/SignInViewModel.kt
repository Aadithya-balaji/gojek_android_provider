package com.appoets.gojek.provider.views.signin

import android.view.View
import android.widget.RadioGroup
import androidx.lifecycle.MutableLiveData
import com.appoets.base.base.BaseViewModel
import com.appoets.gojek.provider.models.LoginResponseModel
import com.appoets.gojek.provider.network.WebApiConstants
import com.appoets.gojek.provider.repository.AppRepository

class SignInViewModel(private val signInNavigator: SignInNavigator) :
        BaseViewModel<SignInViewModel.SignInNavigator>() {

    private val appRepository = AppRepository.instance()
    private var loginLiveData = MutableLiveData<LoginResponseModel>()

    val countryCode = MutableLiveData<String>("+1")
    val phoneNumber = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    init {
        navigator = signInNavigator
    }

    fun onSignInOptionsClick(group: RadioGroup, checkedId: Int) {
        signInNavigator.onCheckedChanged(group, checkedId)
    }

    fun onCountryCodeClick(view: View) {
        signInNavigator.onCountryCodeClicked()
    }

    fun onForgotPasswordClick(view: View) {
        signInNavigator.onForgotPasswordClicked()
    }

    fun onSignUpClick(view: View) {
        signInNavigator.onSignUpClicked()
    }

    fun onSignInClick(view: View) {
        signInNavigator.onSignInClicked()
    }

    fun onGoogleSignInClick(view: View) {
        signInNavigator.onGoogleSignInClicked()
    }

    fun onFacebookLoginClick(view: View) {
        signInNavigator.onFacebookLoginClicked()
    }

    internal fun postLogin(isEmailLogin: Boolean) {
        val params = HashMap<String, String>()
        if (isEmailLogin)
            params[WebApiConstants.EMAIL] = email.value.toString().trim()
        else {
            params[WebApiConstants.COUNTRY_CODE] = countryCode.value.toString().trim().replace("+", "")
            params[WebApiConstants.MOBILE] = phoneNumber.value.toString().trim()
        }
        params[WebApiConstants.PASSWORD] = password.value.toString().trim()
        params[WebApiConstants.SALT_KEY] = "MQ=="
        getCompositeDisposable().add(appRepository.postLogin(this, params))
    }

    fun getLoginObservable() = loginLiveData

    fun getLoginResponseModel() = getLoginObservable().value

    interface SignInNavigator {
        fun onCheckedChanged(group: RadioGroup, checkedId: Int)
        fun onCountryCodeClicked()
        fun onForgotPasswordClicked()
        fun onSignUpClicked()
        fun onSignInClicked()
        fun onGoogleSignInClicked()
        fun onFacebookLoginClicked()
        fun showError(error: String)
    }
}