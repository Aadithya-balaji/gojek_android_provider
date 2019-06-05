package com.gox.partner.views.sign_in

import android.view.View
import android.widget.RadioGroup
import androidx.lifecycle.MutableLiveData
import com.gox.base.BuildConfig
import com.gox.base.base.BaseApplication
import com.gox.base.base.BaseViewModel
import com.gox.base.data.PreferencesKey
import com.gox.partner.models.LoginResponseModel
import com.gox.partner.network.WebApiConstants
import com.gox.partner.repository.AppRepository
import com.gox.partner.utils.Enums

class SignInViewModel : BaseViewModel<SignInViewModel.SignInNavigator>() {

    private val appRepository = AppRepository.instance()
    private var loginLiveData = MutableLiveData<LoginResponseModel>()

    val countryCode = MutableLiveData<String>("+1")
    val phoneNumber = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()


    /*fun onSignInOptionsClick(group: RadioGroup, checkedId: Int) {
        navigator.onCheckedChanged(group, checkedId)
    }*/

    fun changeSigninViaPhone() {
        return navigator.changeSigninViaPhone()
    }

    fun changeSigninViaMail() {
        return navigator.changeSigninViaMail()
    }

    fun onCountryCodeClick(view: View) {
        navigator.onCountryCodeClicked()
    }

    fun onForgotPasswordClick(view: View) {
        navigator.onForgotPasswordClicked()
    }

    fun onSignUpClick(view: View) {
        navigator.onSignUpClicked()
    }

    fun onSignInClick(view: View) {
        navigator.onSignInClicked()
    }

    fun onGoogleSignInClick(view: View) {
        navigator.onGoogleSignInClicked()
    }

    fun onFacebookLoginClick(view: View) {
        navigator.onFacebookLoginClicked()
    }

    internal fun postLogin(isEmailLogin: Boolean) {
        val params = HashMap<String, String>()
        if (isEmailLogin) params[WebApiConstants.Login.EMAIL] = email.value!!.trim()
        else {
            params[WebApiConstants.Login.COUNTRY_CODE] = countryCode.value!!.trim().replace("+", "")
            params[WebApiConstants.Login.MOBILE] = phoneNumber.value!!.trim()
        }
        params[WebApiConstants.Login.PASSWORD] = password.value!!.trim()
        params[WebApiConstants.Login.DEVICE_TYPE] = Enums.DEVICE_TYPE
//        params[WebApiConstants.Login.DEVICE_TOKEN] = readPreferences(PreferencesKey.DEVICE_TOKEN, "123")!!
        params[WebApiConstants.Login.DEVICE_TOKEN] = BaseApplication.getCustomPreference!!.getString(PreferencesKey.DEVICE_TOKEN, "123")!!
        params[WebApiConstants.SALT_KEY] = BuildConfig.SALT_KEY
        getCompositeDisposable().add(appRepository.postLogin(this, params))
    }

    internal fun postSocialLogin(isGoogleSignIn: Boolean, id: String) {
        val params = HashMap<String, String>()
        params[WebApiConstants.SocialLogin.DEVICE_TYPE] = ANDROID
        //  params[WebApiConstants.SocialLogin.DEVICE_TOKEN] = readPreferences(PreferencesKey.DEVICE_TOKEN, "123")!!
        params[WebApiConstants.SocialLogin.DEVICE_TOKEN] = BaseApplication.getCustomPreference!!.getString(PreferencesKey.DEVICE_TOKEN, "123")!!
        params[WebApiConstants.SocialLogin.LOGIN_BY] = if (isGoogleSignIn) {
            LoginType.GOOGLE.value()
        } else {
            LoginType.FACEBOOK.value()
        }
        params[WebApiConstants.SocialLogin.SOCIAL_UNIQUE_ID] = id
        params[WebApiConstants.SALT_KEY] = BuildConfig.SALT_KEY
        getCompositeDisposable().add(appRepository.postSocialLogin(this, params))
    }

    fun getLoginObservable() = loginLiveData

    interface SignInNavigator {
        fun changeSigninViaPhone()
        fun changeSigninViaMail()
        //fun onCheckedChanged(group: RadioGroup, checkedId: Int)
        fun onCountryCodeClicked()
        fun onForgotPasswordClicked()
        fun onSignUpClicked()
        fun onSignInClicked()
        fun onGoogleSignInClicked()
        fun onFacebookLoginClicked()
        fun showAlert(message: String)
        fun showError(error: String)
    }

    internal enum class LoginType {
        GOOGLE {
            override fun value() = "GOOGLE"
        },

        FACEBOOK {
            override fun value() = "FACEBOOK"
        };

        abstract fun value(): String
    }

    companion object {
        const val ANDROID = "ANDROID"
    }
}