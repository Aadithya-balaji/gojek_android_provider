package com.gox.partner.utils.floatingview

import android.app.ActivityManager
import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.ImageView

import androidx.core.app.NotificationCompat

import com.gox.base.R
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.partner.views.dashboard.DashBoardActivity

import jp.co.recruit_lifestyle.android.floatingview.FloatingViewListener
import jp.co.recruit_lifestyle.android.floatingview.FloatingViewManager

class ChatHeadService : Service(), FloatingViewListener {
    private var mFloatingViewManager: FloatingViewManager? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (mFloatingViewManager != null) return START_STICKY
        val metrics = DisplayMetrics()
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(metrics)
        val inflater = LayoutInflater.from(this)
        val iconView = inflater.inflate(R.layout.widget_chathead, null, false) as ImageView
        iconView.setOnClickListener { Log.d(TAG, "Clicked") }
        iconView.setOnClickListener {
            if (isAppIsInBackground(applicationContext)) {
                val isLogged = readPreferences(PreferencesKey.ACCESS_TOKEN, "")
                if (isLogged!!.length > 2) {
                    val intent1 = Intent(applicationContext, DashBoardActivity::class.java)
                    intent1.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent1)
                }
            }
        }

        mFloatingViewManager = FloatingViewManager(this, this)
        mFloatingViewManager!!.setFixedTrashIconImage(R.drawable.ic_trash_fixed)
        mFloatingViewManager!!.setActionTrashIconImage(R.drawable.ic_trash_action)
        mFloatingViewManager!!.setSafeInsetRect(intent.getParcelableExtra(EXTRA_CUTOUT_SAFE_AREA))
        val options = FloatingViewManager.Options()
        options.overMargin = (16 * metrics.density).toInt()
        mFloatingViewManager!!.addViewToWindow(iconView, options)

        startForeground(NOTIFICATION_ID, createNotification(this))

        return START_REDELIVER_INTENT
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

    override fun onDestroy() {
        destroy()
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onFinishFloatingView() {
        stopSelf()
        Log.d(TAG, "onFinishFloatingView")
    }

    override fun onTouchFinished(isFinishing: Boolean, x: Int, y: Int) {
        if (isFinishing) Log.d(TAG, "onTouchFinished:: $isFinishing")
        else Log.d(TAG, "onTouchFinished:: $isFinishing X: $x Y: $y")
    }

    private fun destroy() {
        if (mFloatingViewManager != null) {
            mFloatingViewManager!!.removeAllViewToWindow()
            mFloatingViewManager = null
        }
    }

    companion object {

        private const val TAG = "RRR::ChatHeadService::"
        private const val EXTRA_CUTOUT_SAFE_AREA = "cutout_safe_area"
        private const val NOTIFICATION_ID = 9083150

        private fun createNotification(context: Context): Notification {
            val builder = NotificationCompat.Builder(context, "default_floatingview_channel")
            builder.setWhen(System.currentTimeMillis())
            builder.setContentTitle(context.getString(R.string.app_name))
            builder.setOngoing(true)
            builder.priority = NotificationCompat.PRIORITY_MIN
            builder.setCategory(NotificationCompat.CATEGORY_SERVICE)
            return builder.build()
        }
    }
}