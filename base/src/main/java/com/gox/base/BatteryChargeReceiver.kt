package com.gox.base

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.BatteryManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class BatteryChargeReceiver : BroadcastReceiver() {

    private var scale = -1
    private var level = -1
    private var voltage = -1
    private var temp = -1

    override fun onReceive(context: Context, intent: Intent) {
        level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)
        voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1)

        if (level < 20) notification(context)
    }

    private fun notification(context: Context) {
        val channelId = context.getString(R.string.app_name)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_push)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.low_battery))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            notificationManager.createNotificationChannel(NotificationChannel
            (channelId, "title", NotificationManager.IMPORTANCE_DEFAULT))

        notificationManager.notify(123, notificationBuilder.build())
    }
}