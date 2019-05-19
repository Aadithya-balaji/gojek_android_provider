package com.xjek.provider.fcm

import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.xjek.provider.R

class  FcmService : FirebaseMessagingService(){
    private val tagName = "FCMService"

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        Log.d(tagName, "onNewToken()")
        Log.d(tagName, "FireBaseRegToken: " + token!!)
        Log.e("FCMToken","----"+token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        Log.d(tagName, "msg: $remoteMessage")
        val content = Gson(). toJson(remoteMessage?.data)
        Log.d(tagName, "jsonContent: $content")


        val channelId = getString(R.string.app_name) + System.currentTimeMillis()
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mBuilder = NotificationCompat.Builder(this, channelId)
    }
}