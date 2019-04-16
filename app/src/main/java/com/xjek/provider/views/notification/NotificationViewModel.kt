package com.xjek.provider.views.notification

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.provider.models.NotificationResponse
import com.xjek.provider.repository.AppRepository
import com.xjek.provider.utils.Constant

class NotificationViewModel : BaseViewModel<NotificationNavigator>() {

    private val appRepository = AppRepository.instance()

    var showEmptyView:MutableLiveData<Boolean> = MutableLiveData(false)


    var notificationResponse = MutableLiveData<NotificationResponse>()
    var loadingProgress = MutableLiveData<Boolean>()
    var errorResponse = MutableLiveData<String>()


    fun moveToDetailPage() {
        navigator.goToDetailPage()
    }

    fun getNotificationList() {
        loadingProgress.value = true

        getCompositeDisposable().add(appRepository
                .getNotification(this
                        , Constant.M_TOKEN + readPreferences(PreferencesKey.ACCESS_TOKEN, "")))
    }

}