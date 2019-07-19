package com.gox.partner.views.sign_in

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.gox.base.BuildConfig.SALT_KEY
import com.gox.base.base.BaseApplication
import com.gox.base.base.BaseViewModel
import com.gox.base.data.PreferencesKey
import com.gox.base.repository.ApiListener
import com.gox.partner.models.LoginResponseModel
import com.gox.partner.network.WebApiConstants
import com.gox.partner.repository.AppRepository
import com.gox.partner.utils.Enums

class LoginViewModel : BaseViewModel<LoginViewModel.LoginNavigator>() {

    private val mRepository = AppRepository.instance()
    var loginLiveData = MutableLiveData<LoginResponseModel>()

    val countryCode = MutableLiveData<String>().apply { "+1" }
    val phoneNumber = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    fun changeLoginViaPhone() = navigator.changeLoginViaPhone()

    fun changeLoginViaMail() = navigator.changeLoginViaMail()

    fun onCountryCodeClick(view: View) = navigator.onCountryCodeClicked()

    fun onForgotPasswordClick(view: View) = navigator.onForgotPasswordClicked()

    fun onRegistrationClick(view: View) = navigator.onRegistrationClicked()

    fun onLoginClick(view: View) = navigator.onLoginClicked()

    fun onGoogleLoginClick(view: View) = navigator.onGoogleLoginClicked()

    fun onFacebookLoginClick(view: View) = navigator.onFacebookLoginClicked()

    internal fun postLogin(isEmailLogin: Boolean) {
        val params = HashMap<String, String>()
        if (isEmailLogin) params[WebApiConstants.Login.EMAIL] = email.value!!.trim()
        else {
            params[WebApiConstants.Login.COUNTRY_CODE] = countryCode.value!!.trim().replace("+", "")
            params[WebApiConstants.Login.MOBILE] = phoneNumber.value!!.trim()
        }
        params[WebApiConstants.Login.PASSWORD] = password.value!!.trim()
        params[WebApiConstants.Login.DEVICE_TYPE] = Enums.DEVICE_TYPE
        params[WebApiConstants.Login.DEVICE_TOKEN] = BaseApplication.getCustomPreference!!
                .getString(PreferencesKey.DEVICE_TOKEN, "123")!!
        params["salt_key"] = SALT_KEY
        getCompositeDisposable().add(mRepository.postLogin(object : ApiListener {
            override fun success(successData: Any) {
                loginLiveData.value = successData as LoginResponseModel
            }

            override fun fail(failData: Throwable) {
                navigator.showError(getErrorMessage(failData))
            }

        }, params))
    }

    internal fun postSocialLogin(isGoogleSignIn: Boolean, id: String) {
        val params = HashMap<String, String>()
        params[WebApiConstants.SocialLogin.DEVICE_TYPE] = ANDROID
        params[WebApiConstants.SocialLogin.DEVICE_TOKEN] = BaseApplication.getCustomPreference!!.getString(PreferencesKey.DEVICE_TOKEN, "123")!!
        params[WebApiConstants.SocialLogin.LOGIN_BY] = (if (isGoogleSignIn)
            LoginType.GOOGLE.value() else LoginType.FACEBOOK.value())
        params[WebApiConstants.SocialLogin.SOCIAL_UNIQUE_ID] = id
        params["salt_key"] = SALT_KEY
        getCompositeDisposable().add(mRepository.postSocialLogin(object : ApiListener {
            override fun success(successData: Any) {
                loginLiveData.value = successData as LoginResponseModel
            }

            override fun fail(failData: Throwable) {
                navigator.showError(getErrorMessage(failData))
            }
        }, params))
    }

    interface LoginNavigator {
        fun changeLoginViaPhone()
        fun changeLoginViaMail()
        fun onCountryCodeClicked()
        fun onForgotPasswordClicked()
        fun onRegistrationClicked()
        fun onLoginClicked()
        fun onGoogleLoginClicked()
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