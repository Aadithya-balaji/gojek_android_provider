package com.gox.partner.views.home

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseApplication
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
import com.gox.partner.models.StatusResponseModel
import com.gox.partner.repository.AppRepository

class HomeViewModel : BaseViewModel<HomeNavigator>() {

    val mRepository = AppRepository.instance()

    var loadingProgress = MutableLiveData<Boolean>()

    var onlineStatusLiveData = MutableLiveData<StatusResponseModel>()

    fun showCurrentLocation() = navigator.showCurrentLocation()

    fun changeStatus(view: View) = navigator.changeStatus(view)

    fun changeOnlineStatus(status: String) {
        if (BaseApplication.isNetworkAvailable)
            getCompositeDisposable().add(mRepository.changeOnlineStatus(object : ApiListener {
                override fun success(successData: Any) {
                    onlineStatusLiveData.value = successData as StatusResponseModel
                    loadingProgress.value = false
                }

                override fun fail(failData: Throwable) {
                    navigator.showErrorMessage(getErrorMessage(failData))
                    loadingProgress.value = false
                }
            }, status))
    }
}