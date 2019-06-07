package com.gox.partner.views.reset_password

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.gox.base.BuildConfig
import com.gox.base.BuildConfig.SALT_KEY
import com.gox.base.base.BaseViewModel
import com.gox.partner.models.ResetPasswordResponseModel
import com.gox.partner.network.WebApiConstants
import com.gox.partner.repository.AppRepository

class ResetPasswordViewModel : BaseViewModel<ResetPasswordViewModel.ResetPasswordNavigator>() {

    private val appRepository = AppRepository.instance()
    private var resetPasswordLiveData = MutableLiveData<ResetPasswordResponseModel>()
    internal lateinit var accountType: String
    internal lateinit var countryCode: String
    internal lateinit var username: String
    internal lateinit var receivedOtp: String

    val otp = MutableLiveData<String>()
    val newPassword = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()

    fun getResetPasswordObservable() = resetPasswordLiveData

    fun onOtpClick(view: View) {
        navigator.onOtpClicked()
    }

    fun onResetPasswordClick(view: View) {
        navigator.onResetPasswordClicked()
    }

    internal fun postResetPassword() {
        val params = HashMap<String, String>()
        params["salt_key"] = SALT_KEY
        params[WebApiConstants.ResetPassword.OTP] = otp.value!!.trim()
        params[WebApiConstants.ResetPassword.PASSWORD] = newPassword.value!!.trim()
        params[WebApiConstants.ResetPassword.PASSWORD_CONFIRMATION] = confirmPassword.value!!.trim()
        params[WebApiConstants.ResetPassword.USERNAME] = username
        if (countryCode.isNullOrBlank()) {
            params[WebApiConstants.ResetPassword.ACCOUNT_TYPE] =
                    ResetPasswordViewModel.AccountType.EMAIL.value()
        } else {
            params[WebApiConstants.ResetPassword.ACCOUNT_TYPE] =
                    ResetPasswordViewModel.AccountType.MOBILE.value()
            params[WebApiConstants.ResetPassword.COUNTRY_CODE] = countryCode.replace("+", "")
        }
        getCompositeDisposable().add(appRepository.postResetPassword(this, params))
    }

    interface ResetPasswordNavigator {
        fun onOtpClicked()
        fun onResetPasswordClicked()
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