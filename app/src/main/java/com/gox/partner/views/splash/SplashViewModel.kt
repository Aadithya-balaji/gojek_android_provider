package com.gox.partner.views.splash

import androidx.lifecycle.MutableLiveData
import com.gox.base.BuildConfig
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
import com.gox.partner.models.ConfigResponseModel
import com.gox.partner.repository.AppRepository
import java.text.DecimalFormat

class SplashViewModel : BaseViewModel<SplashViewModel.SplashNavigator>() {

    private val mRepository = AppRepository.instance()
    var configLiveData = MutableLiveData<ConfigResponseModel>()

    internal fun getConfig() {
        val params = HashMap<String, String>()
        params["salt_key"] = BuildConfig.SALT_KEY
        getCompositeDisposable().add(mRepository.getConfig(object : ApiListener {
            override fun success(successData: Any) {
                configLiveData.value = successData as ConfigResponseModel
            }

            override fun fail(failData: Throwable) {
                navigator.showError(getErrorMessage(failData))
            }
        }, params))
    }

    interface SplashNavigator {
        fun showError(error: String)
    }
}