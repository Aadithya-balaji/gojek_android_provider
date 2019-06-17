package com.gox.partner.views.change_password

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
import com.gox.partner.models.ChangePasswordResponseModel
import com.gox.partner.network.WebApiConstants
import com.gox.partner.repository.AppRepository

class ChangePasswordViewModel : BaseViewModel<ChangePasswordViewModel.ChangePasswordNavigator>() {

    private val mRepository = AppRepository.instance()
    private var changePasswordLiveData = MutableLiveData<ChangePasswordResponseModel>()

    val oldPassword = MutableLiveData<String>()
    val newPassword = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()

    fun onChangePasswordClick(view: View) = navigator.onChangePasswordClicked()

    internal fun postChangePassword() {
        val params = HashMap<String, String>()
        params[WebApiConstants.ChangePassword.OLD_PASSWORD] = oldPassword.value!!.trim()
        params[WebApiConstants.ChangePassword.PASSWORD] = newPassword.value!!.trim()
        getCompositeDisposable().add(mRepository.postChangePassword(object : ApiListener {
            override fun success(successData: Any) {
                getChangePasswordObservable().value = successData as ChangePasswordResponseModel
            }

            override fun fail(failData: Throwable) {
                navigator.showError(getErrorMessage(failData))
            }
        }, params))
    }

    fun getChangePasswordObservable() = changePasswordLiveData

    interface ChangePasswordNavigator {
        fun onChangePasswordClicked()
        fun showError(error: String)
    }
}