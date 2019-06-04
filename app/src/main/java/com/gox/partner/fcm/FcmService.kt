package com.gox.partner.fcm

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioManager
import android.media.RingtoneManager
import android.os.Build
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.gox.base.BuildConfig
import com.gox.base.R
import com.gox.base.base.BaseApplication
import com.gox.base.data.PreferencesKey
import com.gox.partner.views.dashboard.DashBoardActivity
import com.gox.partner.views.splash.SplashActivity

class FcmService : FirebaseMessagingService() {

    private val tagName = "FCMService"

    private lateinit var mUrlPersistence: SharedPreferences

    @SuppressLint("CommitPrefEdits")
    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        mUrlPersistence = BaseApplication.run { getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE) }

        Log.d(tagName, "FireBaseRegToken: " + token!!)
        Log.e("FCMToken", "----$token")

        mUrlPersistence.edit().putString(PreferencesKey.DEVICE_TOKEN, token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        println("RRR push notification:: $remoteMessage")
        if (remoteMessage?.notification != null)
            sendNotification(remoteMessage.notification?.body.toString())
        else sendNotification(remoteMessage?.data?.get("message").toString())
    }

    private fun sendNotification(messageBody: String) {
        println("RRR push messageBody = $messageBody")
        val intent = Intent(this, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val channelId = getString(R.string.app_name)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    private fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningProcesses = am.runningAppProcesses
        for (processInfo in runningProcesses)
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
                for (activeProcess in processInfo.pkgList)
                    if (activeProcess == context.packageName) isInBackground = false

        return isInBackground
    }

    private fun sendNotificationProlonged(messageBody: String) {

//        val pm = applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager
//        if (!pm.isScreenOn) {
//            println("RRR MyFireBaseMessagingService.sendNotificationProlonged")
//            val screenLock = (getSystemService(Context.POWER_SERVICE) as PowerManager).newWakeLock(
//                    PowerManager.PARTIAL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.ON_AFTER_RELEASE, "TAG")
//            screenLock.acquire()
//
//        }
//        val intent = Intent(this, SplashActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT)
//
//        val channelId = getString(R.string.default_notification_channel_id)
//        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val notificationBuilder = NotificationCompat.Builder(this, channelId)
//                .setSmallIcon(R.mipmap.ic_launcher_round)
//                .setContentTitle(getString(R.string.app_name))
//                .setContentText(messageBody)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent)
//
//        val mNotification = notificationBuilder.build()
//        mNotification.flags = Notification.DEFAULT_LIGHTS or Notification.FLAG_AUTO_CANCEL or Notification.DEFAULT_SOUND
//
//        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(channelId,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_HIGH)
//            nm.createNotificationChannel(channel)
//        }
//
//        nm.notify(notificationId++, mNotification)
    }

    private fun isBackground(context: Context): Boolean {
        var isInBackground = true
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            val runningProcesses = am.runningAppProcesses
            for (processInfo in runningProcesses)
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
                    for (activeProcess in processInfo.pkgList)
                        if (activeProcess == context.packageName) isInBackground = false
        } else {
            val taskInfo = am.getRunningTasks(1)
            val componentInfo = taskInfo[0].topActivity
            if (componentInfo.packageName == context.packageName)
                isInBackground = false
        }

        return isInBackground
    }

    fun isCallActive(context: Context): Boolean {
        val manager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return manager.mode == AudioManager.MODE_IN_CALL
    }

    fun isLocked(context: Context): Boolean {
        val myKM = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        return myKM.isKeyguardLocked
    }

    private fun restartApp() {
        println("RRR MyFireBaseMessagingService.restartApp")
        val intent = Intent(this, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}