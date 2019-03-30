package com.appoets.gojek.provider.views.change_password

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.appoets.base.base.BaseViewModel
import com.appoets.gojek.provider.models.ChangePasswordResponseModel
import com.appoets.gojek.provider.network.WebApiConstants
import com.appoets.gojek.provider.repository.AppRepository
import com.appoets.gojek.provider.utils.Constant

class ChangePasswordViewModel(private val changePasswordNavigator: ChangePasswordNavigator) :
        BaseViewModel<ChangePasswordViewModel.ChangePasswordNavigator>() {

    private val appRepository = AppRepository.instance()
    private var changePasswordLiveData = MutableLiveData<ChangePasswordResponseModel>()

    val oldPassword = MutableLiveData<String>()
    val newPassword = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()

    fun onBackClick(view: View) {
        changePasswordNavigator.onBackClicked()
    }

    fun onChangePasswordClick(view: View) {
        changePasswordNavigator.onChangePasswordClicked()
    }

    internal fun postChangePassword() {
        val token = StringBuilder("Bearer ").append(Constant.accessToken).toString()
        val params = HashMap<String, String>()
        params[WebApiConstants.OLD_PASSWORD] = oldPassword.value!!.trim()
        params[WebApiConstants.PASSWORD] = newPassword.value!!.trim()
        getCompositeDisposable().add(appRepository.postChangePassword(this, token, params))
    }

    fun getChangePasswordObservable() = changePasswordLiveData

    fun getChangePasswordResponseModel() = getChangePasswordObservable().value

    interface ChangePasswordNavigator {
        fun onBackClicked()
        fun onChangePasswordClicked()
        fun showError(error: String)
    }
}