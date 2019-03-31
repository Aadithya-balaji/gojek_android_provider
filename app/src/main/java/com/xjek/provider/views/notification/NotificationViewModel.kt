package com.xjek.provider.views.notification

import com.xjek.base.base.BaseViewModel

class NotificationViewModel:BaseViewModel<NotificationNavigator>(){
    fun openDetailPage(){
        navigator.gotoNotificationDetailPage()
    }
 }


