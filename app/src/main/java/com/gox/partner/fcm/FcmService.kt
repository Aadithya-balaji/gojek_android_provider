package com.gox.partner.fcm

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioManager
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.gox.base.BuildConfig
import com.gox.base.R
import com.gox.base.base.BaseApplication
import com.gox.base.data.PreferencesHelper
import com.gox.base.data.PreferencesKey
import com.gox.partner.views.splash.SplashActivity

class FcmService : FirebaseMessagingService() {

    private var notificationId = 100

    private lateinit var mUrlPersistence: SharedPreferences

    @SuppressLint("CommitPrefEdits")
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        mUrlPersistence = BaseApplication.run { getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE) }
        mUrlPersistence.edit().putString(PreferencesKey.DEVICE_TOKEN, token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val notificationData = Gson().fromJson(remoteMessage.data!!["custom"], NotificationDataModel::class.java)

        if (notificationData.message!!.topic!!.contains("incoming_request"))
            sendProlongedNotification(notificationData)
        else sendNotification(notificationData)

        if (notificationData.message!!.topic!!.contains("incoming_request")
                && !isBackground(applicationContext)
                && !isLocked(applicationContext)
                && !isCallActive(applicationContext) && !PreferencesHelper
                        .get(PreferencesKey.ACCESS_TOKEN, "")
                        .equals("")) restartApp()
    }

    private fun sendNotification(notificationData: NotificationDataModel) {

        val intent = Intent(this, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val channelId = getString(R.string.app_name)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_push)
                .setColor(ContextCompat.getColor(applicationContext,R.color.colorPrimary))
                .setContentTitle(notificationData.message!!.notification!!.title)
                .setContentText(notificationData.message!!.notification!!.body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) notificationManager.createNotificationChannel(
                NotificationChannel(channelId, getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT))

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    private fun sendProlongedNotification(notificationData: NotificationDataModel) {
        println("RRR push notificationData = $notificationData")

        val intent = Intent(this, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val channelId = getString(com.gox.partner.R.string.app_name)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(notificationData.message!!.notification!!.title)
                .setContentText(notificationData.message!!.notification!!.body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        val mNotification = notificationBuilder.build()
        mNotification.flags = Notification.DEFAULT_LIGHTS or Notification.FLAG_AUTO_CANCEL or Notification.DEFAULT_SOUND

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) notificationManager.createNotificationChannel(
                NotificationChannel(channelId, getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT))

        notificationManager.notify(notificationId, mNotification)
    }

    private fun isBackground(context: Context): Boolean {
        var isInBackground = true
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningProcesses = am.runningAppProcesses
        for (processInfo in runningProcesses)
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
                for (activeProcess in processInfo.pkgList)
                    if (activeProcess == context.packageName) isInBackground = false
        return isInBackground
    }

    private fun isCallActive(context: Context): Boolean {
        val manager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return manager.mode == AudioManager.MODE_IN_CALL
    }

    private fun isLocked(context: Context): Boolean {
        val myKM = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        return myKM.isKeyguardLocked
    }

    private fun restartApp() {
        val intent = Intent(this, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}