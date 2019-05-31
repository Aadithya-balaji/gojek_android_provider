package com.gox.partner.views.change_password

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.partner.models.ChangePasswordResponseModel
import com.gox.partner.network.WebApiConstants
import com.gox.partner.repository.AppRepository

class ChangePasswordViewModel : BaseViewModel<ChangePasswordViewModel.ChangePasswordNavigator>() {

    private val appRepository = AppRepository.instance()
    private var changePasswordLiveData = MutableLiveData<ChangePasswordResponseModel>()

    val oldPassword = MutableLiveData<String>()
    val newPassword = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()

    fun onChangePasswordClick(view: View) {
        navigator.onChangePasswordClicked()
    }

    internal fun postChangePassword() {
        val token = StringBuilder("Bearer ")
                .append(readPreferences<String>(PreferencesKey.ACCESS_TOKEN))
                .toString()
        val params = HashMap<String, String>()
        params[WebApiConstants.ChangePassword.OLD_PASSWORD] = oldPassword.value!!.trim()
        params[WebApiConstants.ChangePassword.PASSWORD] = newPassword.value!!.trim()
        getCompositeDisposable().add(appRepository.postChangePassword(this, token, params))
    }

    fun getChangePasswordObservable() = changePasswordLiveData

    interface ChangePasswordNavigator {
        fun onChangePasswordClicked()
        fun showError(error: String)
    }
}