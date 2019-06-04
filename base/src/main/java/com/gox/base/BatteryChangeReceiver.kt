package com.gox.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager

class BatteryChangeReceiver : BroadcastReceiver() {

    private var scale = -1
    private var level = -1
    private var voltage = -1
    private var temp = -1

    override fun onReceive(context: Context, intent: Intent) {
        level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)
        voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1)

        if (level < 92) {
            println("RRR :: level = $level")
            showNotification()
        }
    }

    private fun showNotification() {

    }
}