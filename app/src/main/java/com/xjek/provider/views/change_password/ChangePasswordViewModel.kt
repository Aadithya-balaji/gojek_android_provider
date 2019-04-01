package com.xjek.provider.views.change_password

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.provider.models.ChangePasswordResponseModel
import com.xjek.provider.network.WebApiConstants
import com.xjek.provider.repository.AppRepository
import com.xjek.provider.utils.Constant


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