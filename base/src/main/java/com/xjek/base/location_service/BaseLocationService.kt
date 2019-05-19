package com.xjek.base.location_service

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.FirebaseDatabase
import com.xjek.base.data.Constants.BroadCastTypes.BASE_BROADCAST
import com.xjek.base.data.PreferencesKey
import com.xjek.base.data.PreferencesKey.FIRE_BASE_PROVIDER_IDENTITY
import com.xjek.base.extensions.readPreferences
import com.xjek.base.persistence.AppDatabase
import com.xjek.base.persistence.LocationPointsEntity
import java.io.Serializable
import java.text.DateFormat
import java.util.*
import com.xjek.base.R


class BaseLocationService : Service() {

    //      Guindy Location :: 12.998219, 80.205836
    //      Tranxit         :: 13.058687, 80.253300

    private val channelId = "channel_01"

    private val checkInterval = 5000

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
                    .addAction(R.drawable.star_blue, getString(R.string.app_name), null)
                    .setContentText(mLocation!!.toString())
                    .setContentTitle(DateFormat.getDateTimeInstance().format(Date()))
                    .setOngoing(true)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setSmallIcon(R.drawable.star_blue)
                    .setTicker(mLocation!!.toString())
                    .setWhen(System.currentTimeMillis())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                builder.setChannelId(channelId)

            return builder.build()
        }

    override fun onCreate() {
        println("RRRR:: BaseLocationService.onCreate")
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        h = Handler()
        r = Runnable {
            h!!.postDelayed(r, checkInterval.toLong())
            streamLocation()
        }

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                onNewLocation(locationResult!!.lastLocation)
                if (h != null) {
                    h!!.removeCallbacks(r)
                    h!!.postDelayed(r, checkInterval.toLong())
                }
            }
        }

        val displacement: Long = 10
        val updateInterval: Long = 3000
        val fastestUpdateInterval = updateInterval / 2

        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = updateInterval
        mLocationRequest!!.fastestInterval = fastestUpdateInterval
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest!!.smallestDisplacement = displacement.toFloat()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission missing...", Toast.LENGTH_SHORT).show()
                return
            }

        mFusedLocationClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback!!, Looper.myLooper())

        streamLocation()

        val thread = HandlerThread("BaseLocationService")
        thread.start()
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val mChannel = NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_DEFAULT)
            mNotificationManager!!.createNotificationChannel(mChannel)
        }

        requestLocationUpdates()
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
                    println("RRRR:: TaxiDashBoardActivity received $mLocation")
                    onNewLocation(mLocation!!)
                } else println("RRRR:: Failed to get location.")
                println("RRRR:: streamLocation::task = $task")
                val loc = Location("dummyprovider")
                loc.setLatitude(0.0)
                loc.setLongitude(0.0)
                onNewLocation(loc)
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
        val provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(!provider.contains("gps")){
            intent.putExtra("ISGPS_EXITS", false)
        }else{
            intent.putExtra("ISGPS_EXITS", true)

        }

    LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        val providerId = readPreferences(FIRE_BASE_PROVIDER_IDENTITY, 0)
        val point = LocationPointsEntity(null, location.latitude, location.longitude,
                DateFormat.getDateTimeInstance().format(Date()))
        try {
            if (providerId!! > 0 && readPreferences(PreferencesKey.CAN_SAVE_LOCATION, false)!!) {
                FirebaseDatabase.getInstance().getReference("providerId$providerId").setValue(point)
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

    private fun requestLocationUpdates() {
        println("RRRR:: BaseLocationService.requestLocationUpdates")
        startService(Intent(applicationContext, BaseLocationService::class.java))
        try {
            mFusedLocationClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback!!, Looper.myLooper())
        } catch (unlikely: SecurityException) {
            println("RRRR:: Lost location permission. Could not request updates. $unlikely")
        }
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

data class LocationModel(
        var lat: Double? = 0.0,
        var lng: Double? = 0.0,
        var time: String = ""
) : Serializable