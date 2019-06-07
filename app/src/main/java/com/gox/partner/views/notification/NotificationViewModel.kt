package com.gox.partner.views.notification

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.data.Constants
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.partner.models.NotificationResponse
import com.gox.partner.repository.AppRepository

class NotificationViewModel : BaseViewModel<NotificationNavigator>() {

    private val appRepository = AppRepository.instance()

    var showEmptyView: MutableLiveData<Boolean> = MutableLiveData(false)


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
                        , Constants.M_TOKEN + readPreferences(PreferencesKey.ACCESS_TOKEN, "")))
    }

}