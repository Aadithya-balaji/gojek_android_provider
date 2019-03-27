package com.appoets.gojek.provider.views.notification

import com.appoets.base.base.BaseViewModel

class NotificationViewModel:BaseViewModel<NotificationNavigator>(){
    fun openDetailPage(){
        navigator.gotoNotificationDetailPage()
    }
 }


