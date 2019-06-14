package com.gox.partner.views.home

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseApplication
import com.gox.base.base.BaseViewModel
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.partner.models.StatusResponseModel
import com.gox.partner.repository.AppRepository

class HomeViewModel : BaseViewModel<HomeNavigator>() {

    val appRepository = AppRepository.instance()

    var showLoading = MutableLiveData<Boolean>()

    var onlineStatusLiveData = MutableLiveData<StatusResponseModel>()

    fun showCurrentLocation() = navigator.showCurrentLocation()

    fun changeStatus(view: View) = navigator.changeStatus(view)

    fun changeOnlineStatus(status: String) {
        if (BaseApplication.isNetworkAvailable)
            getCompositeDisposable().add(appRepository.changeOnlineStatus
            (this, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), status))
    }
}