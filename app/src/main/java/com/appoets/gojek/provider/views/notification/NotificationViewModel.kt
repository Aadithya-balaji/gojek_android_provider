package com.appoets.gojek.provider.views.notification

import com.appoets.basemodule.base.BaseViewModel

class NotificationViewModel:BaseViewModel<NotificationNavigator>(){
    fun openDetailPage(){
        navigator.gotoNotificationDetailPage()
    }
 }


