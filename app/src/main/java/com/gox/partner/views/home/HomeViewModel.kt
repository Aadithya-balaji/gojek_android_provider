package com.gox.partner.views.home

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseApplication
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
import com.gox.partner.models.ProfileResponse
import com.gox.partner.models.StatusResponseModel
import com.gox.partner.repository.AppRepository

class HomeViewModel : BaseViewModel<HomeNavigator>() {

    val mRepository = AppRepository.instance()

    var showLoading = MutableLiveData<Boolean>()

    var onlineStatusLiveData = MutableLiveData<StatusResponseModel>()

    var mProfileResponse = MutableLiveData<ProfileResponse>()


    fun showCurrentLocation() = navigator.showCurrentLocation()

    fun changeStatus(view: View) = navigator.changeStatus(view)

    fun changeOnlineStatus(status: String) {
        if (BaseApplication.isNetworkAvailable)
            getCompositeDisposable().add(mRepository.changeOnlineStatus(object : ApiListener {
                override fun success(successData: Any) {
                    onlineStatusLiveData.value = successData as StatusResponseModel
                    showLoading.postValue(false)
                }

                override fun fail(failData: Throwable) {
                    navigator.showErrorMessage(getErrorMessage(failData))
                    showLoading.postValue(false)
                }
            }, status))
    }

    fun getProfile() {
        if (BaseApplication.isNetworkAvailable)
            getCompositeDisposable().add(mRepository.getProviderProfile(object : ApiListener {
                override fun success(successData: Any) {
                    mProfileResponse.value = successData as ProfileResponse
                }

                override fun fail(failData: Throwable) {
                }
            }))
    }
}