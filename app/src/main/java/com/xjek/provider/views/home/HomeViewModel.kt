package com.xjek.provider.views.home

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.provider.models.StatusResponseModel
import com.xjek.provider.repository.AppRepository

class HomeViewModel : BaseViewModel<HomeNavigator>() {

    val appRepository = AppRepository.instance()

    var showLoading = MutableLiveData<Boolean>()

    var onlineStatusLiveData = MutableLiveData<StatusResponseModel>()



    fun changeStatus(view: View) {
        navigator.changeStatus(view)
    }

    fun changeOnlineStatus(status: String) {
        getCompositeDisposable().add(appRepository.changeOnlineStatus(this, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), status))
    }
}