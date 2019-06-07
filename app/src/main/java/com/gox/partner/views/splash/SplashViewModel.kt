package com.gox.partner.views.splash

import androidx.lifecycle.MutableLiveData
import com.gox.base.BuildConfig
import com.gox.base.base.BaseViewModel
import com.gox.partner.models.ConfigResponseModel
import com.gox.partner.network.WebApiConstants
import com.gox.partner.repository.AppRepository

class SplashViewModel : BaseViewModel<SplashViewModel.SplashNavigator>() {

    private val appRepository = AppRepository.instance()
    private var configLiveData = MutableLiveData<ConfigResponseModel>()

    fun getConfigObservable() = configLiveData

    internal fun getConfig() {
        val params = HashMap<String, String>()
        params["salt_key"] = BuildConfig.SALT_KEY
        getCompositeDisposable().add(appRepository.getConfig(this, params))
    }

    interface SplashNavigator {
        fun showError(error: String)
    }
}