package com.gox.base.location_service

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.FirebaseDatabase
import com.gox.base.R
import com.gox.base.data.Constants.BroadCastTypes.BASE_BROADCAST
import com.gox.base.data.PreferencesKey
import com.gox.base.data.PreferencesKey.FIRE_BASE_PROVIDER_IDENTITY
import com.gox.base.extensions.readPreferences
import com.gox.base.persistence.AppDatabase
import com.gox.base.persistence.LocationPointsEntity
import com.gox.base.utils.ViewUtils
import java.text.DateFormat
import java.util.*

class BaseLocationService : Service() {

    private val channelId = "channel_01"

    private val checkInterval = 10000

    private var r: Runnable? = null
    private var h: Handler? = null

    private var mNotificationManager: NotificationManager? = null
    private var mLocationRequest: LocationRequest? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLocationCallback: LocationCallback? = null

    private var mLocation: Location? = null

    private val notification: Notification
        get() {
            val intent = Intent(this, BaseLocationService::class.java)
            intent.putExtra(NOTIFICATION, true)

            val builder = NotificationCompat.Builder(this, "123456")
                    .addAction(R.drawable.ic_push, getString(R.string.app_name), null)
                    .setContentText(mLocation!!.toString())
                    .setContentTitle(DateFormat.getDateTimeInstance().format(Date()))
                    .setOngoing(true)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setSmallIcon(R.drawable.ic_push)
                    .setTicker(mLocation!!.toString())
                    .setWhen(System.currentTimeMillis())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                builder.setChannelId(channelId)

            return builder.build()
        }

    override fun onCreate() {
        println("RRRR:: BaseLocationService.onCreate")
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                println("RRRR:: BaseLocationService.LocationCallBack")
                super.onLocationResult(locationResult)
                onNewLocation(locationResult!!.lastLocation)
            }
        }

        val displacement: Long = 50
        val updateInterval: Long = 7000
        val fastestUpdateInterval = updateInterval / 2

        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = updateInterval
        mLocationRequest!!.fastestInterval = fastestUpdateInterval
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest!!.smallestDisplacement = displacement.toFloat()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ViewUtils.showNormalToast(this, getText(R.string.location_permissing_missing) as String)
                return
            }

        mFusedLocationClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback!!, Looper.myLooper())
        streamLocation()
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val mChannel = NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_DEFAULT)
            mNotificationManager!!.createNotificationChannel(mChannel)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        println("RRRR:: BaseLocationService.onStartCommand")
        val startedFromNotification = intent.getBooleanExtra(NOTIFICATION, false)
        if (startedFromNotification) {
            removeLocationUpdates()
            stopSelf()
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        println("RRRR:: BaseLocationService.onDestroy")
        mFusedLocationClient!!.removeLocationUpdates(mLocationCallback!!)
    }

    private fun streamLocation() {
        try {
            mFusedLocationClient!!.lastLocation.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    mLocation = task.result
                    println("RRRR:: mFusedLocationClient received $mLocation")
                    onNewLocation(mLocation!!)
                } else println("RRRR:: Failed to get location.")
            }
        } catch (unlikely: SecurityException) {
            println("RRRR:: Lost location permission.$unlikely")
            unlikely.printStackTrace()
        }
    }

    private fun onNewLocation(location: Location) {
        println("RRRR:: BaseLocationService.onNewLocation :: BROADCAST :: $BROADCAST")
        val intent = Intent(BROADCAST)
        intent.putExtra(EXTRA_LOCATION, location)
        println("RRRR:: intent :: $intent")
        val provider = Settings.Secure.getString(contentResolver, Settings.Secure.LOCATION_PROVIDERS_ALLOWED)
        if (!provider.contains("gps")) intent.putExtra("ISGPS_EXITS", false)
        else intent.putExtra("ISGPS_EXITS", true)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        val providerId = readPreferences(FIRE_BASE_PROVIDER_IDENTITY, 0)
        val point = LocationPointsEntity(null, location.latitude, location.longitude, DateFormat.getDateTimeInstance().format(Date()))
        try {
            if (providerId!! > 0 && readPreferences(PreferencesKey.CAN_SEND_LOCATION, false)!!) {
                FirebaseDatabase.getInstance().getReference("providerId$providerId").setValue(point)
                if (readPreferences(PreferencesKey.CAN_SAVE_LOCATION, false)!!)
                    AppDatabase.getAppDataBase(this)!!.locationPointsDao().insertPoint(point)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val notificationId = 12345678
        if (serviceIsRunningInForeground(this))
            mNotificationManager!!.notify(notificationId, notification)
    }

    private fun distBt(a: LatLng, b: LatLng): Double {
        val startPoint = Location("start")
        startPoint.latitude = a.latitude
        startPoint.longitude = a.longitude

        val endPoint = Location("end")
        endPoint.latitude = b.latitude
        endPoint.longitude = b.longitude
        return startPoint.distanceTo(endPoint).toDouble()
    }

    private fun serviceIsRunningInForeground(context: Context): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE))
            if (javaClass.name == service.service.className)
                if (service.foreground) return true
        return false
    }


    private fun removeLocationUpdates() {
        println("RRRR:: BaseLocationService.removeLocationUpdates")
        try {
            mFusedLocationClient!!.removeLocationUpdates(mLocationCallback!!)
            stopSelf()
        } catch (unlikely: SecurityException) {
            println("RRRR:: Lost location permission. Could not remove updates. $unlikely")
        }
    }

    companion object {
        var BROADCAST = BASE_BROADCAST
        const val EXTRA_LOCATION = "LOCATION"
        const val NOTIFICATION = "NOTIFICATION"
    }
}