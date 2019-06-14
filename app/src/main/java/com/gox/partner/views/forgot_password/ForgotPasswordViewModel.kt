package com.gox.partner.views.forgot_password

import android.view.View
import android.widget.RadioGroup
import androidx.lifecycle.MutableLiveData
import com.gox.base.BuildConfig
import com.gox.base.base.BaseViewModel
import com.gox.partner.models.ForgotPasswordResponseModel
import com.gox.partner.network.WebApiConstants
import com.gox.partner.repository.AppRepository

class ForgotPasswordViewModel : BaseViewModel<ForgotPasswordViewModel.ForgotPasswordNavigator>() {

    private val appRepository = AppRepository.instance()
    private var forgotPasswordLiveData = MutableLiveData<ForgotPasswordResponseModel>()

    val countryCode = MutableLiveData<String>("+1")
    val phoneNumber = MutableLiveData<String>()
    val email = MutableLiveData<String>()

    fun onResetOptionsClick(group: RadioGroup, checkedId: Int) =
            navigator.onCheckedChanged(group, checkedId)

    fun onCountryCodeClick(view: View) = navigator.onCountryCodeClicked()

    fun onForgotPasswordClick(view: View) = navigator.onForgotPasswordClicked()

    internal fun postForgotPassword(isEmailLogin: Boolean) {
        val params = HashMap<String, String>()
        params["salt_key"] = BuildConfig.SALT_KEY
        if (isEmailLogin) {
            params[WebApiConstants.ForgotPassword.ACCOUNT_TYPE] = AccountType.EMAIL.value()
            params[WebApiConstants.ForgotPassword.EMAIL] = email.value!!.trim()
        } else {
            params[WebApiConstants.ForgotPassword.ACCOUNT_TYPE] = AccountType.MOBILE.value()
            params[WebApiConstants.ForgotPassword.COUNTRY_CODE] = countryCode.value!!.trim()
            params[WebApiConstants.ForgotPassword.MOBILE] = phoneNumber.value!!.trim()
        }
        getCompositeDisposable().add(appRepository.postForgotPassword(this, params))
    }

    fun getForgotPasswordObservable() = forgotPasswordLiveData

    interface ForgotPasswordNavigator {
        fun onCheckedChanged(group: RadioGroup, checkedId: Int)
        fun onCountryCodeClicked()
        fun onForgotPasswordClicked()
        fun showError(error: String)
    }

    internal enum class AccountType {
        MOBILE {
            override fun value() = "mobile"
        },

        EMAIL {
            override fun value() = "email"
        };

        abstract fun value(): String
    }
}