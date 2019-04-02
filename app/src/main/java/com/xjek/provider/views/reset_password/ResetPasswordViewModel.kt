package com.xjek.provider.views.reset_password

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.xjek.base.BuildConfig
import com.xjek.base.base.BaseViewModel
import com.xjek.provider.models.ResetPasswordResponseModel
import com.xjek.provider.network.WebApiConstants
import com.xjek.provider.repository.AppRepository

class ResetPasswordViewModel : BaseViewModel<ResetPasswordViewModel.ResetPasswordNavigator>() {

    private val appRepository = AppRepository.instance()
    private var resetPasswordLiveData = MutableLiveData<ResetPasswordResponseModel>()
    internal lateinit var accountType: String
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

    internal fun postResetPassword(isEmailReset: Boolean) {
        val params = HashMap<String, String>()
        params[WebApiConstants.SALT_KEY] = BuildConfig.SALT_KEY
        params[WebApiConstants.ResetPassword.OTP] = otp.value!!.trim()
        params[WebApiConstants.ResetPassword.PASSWORD] = otp.value!!.trim()
        params[WebApiConstants.ResetPassword.PASSWORD_CONFIRMATION] = otp.value!!.trim()
        if (isEmailReset) {
            params[WebApiConstants.ResetPassword.ACCOUNT_TYPE] =
                    ResetPasswordViewModel.AccountType.EMAIL.value()
            params[WebApiConstants.ResetPassword.USERNAME] = username
        } else {
            params[WebApiConstants.ResetPassword.ACCOUNT_TYPE] =
                    ResetPasswordViewModel.AccountType.MOBILE.value()
//            params[WebApiConstants.ResetPassword.COUNTRY_CODE] = countryCode.value!!.trim()
            params[WebApiConstants.ResetPassword.USERNAME] = username
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