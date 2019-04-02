package com.xjek.provider.views.splash

import androidx.lifecycle.MutableLiveData
import com.xjek.base.BuildConfig
import com.xjek.base.base.BaseViewModel
import com.xjek.provider.models.ConfigResponseModel
import com.xjek.provider.network.WebApiConstants
import com.xjek.provider.repository.AppRepository

class SplashViewModel : BaseViewModel<SplashViewModel.SplashNavigator>() {

    private val appRepository = AppRepository.instance()
    private var configLiveData = MutableLiveData<ConfigResponseModel>()

    fun getConfigObservable() = configLiveData

    internal fun getConfig() {
        val params = HashMap<String, String>()
        params[WebApiConstants.SALT_KEY] = BuildConfig.SALT_KEY
        getCompositeDisposable().add(appRepository.getConfig(this, params))
    }

    interface SplashNavigator {
        fun showError(error: String)
    }
}