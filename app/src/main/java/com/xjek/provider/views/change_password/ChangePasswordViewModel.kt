package com.xjek.provider.views.change_password

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.provider.models.ChangePasswordResponseModel
import com.xjek.provider.network.WebApiConstants
import com.xjek.provider.repository.AppRepository

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