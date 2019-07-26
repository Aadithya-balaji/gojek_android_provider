package com.gox.partner.views.notification

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
import com.gox.partner.models.NotificationResponse
import com.gox.partner.repository.AppRepository

class NotificationViewModel : BaseViewModel<NotificationNavigator>() {

    private val mRepository = AppRepository.instance()

    var showEmptyView: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { false }

    var notificationResponse = MutableLiveData<NotificationResponse>()
    var showLoading = MutableLiveData<Boolean>()
    var errorResponse = MutableLiveData<String>()

    fun getNotificationList() {
        showLoading.value = true
        getCompositeDisposable().add(mRepository.getNotification(object : ApiListener {
            override fun success(successData: Any) {
                notificationResponse.value = successData as NotificationResponse
                showLoading.postValue(false)
            }

            override fun fail(failData: Throwable) {
                showLoading.postValue(false)
                errorResponse.value = getErrorMessage(failData)
            }
        }))
    }

}