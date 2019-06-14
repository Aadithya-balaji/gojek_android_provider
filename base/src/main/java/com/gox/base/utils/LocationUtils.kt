package com.gox.base.utils

import android.Manifest
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.text.TextUtils
import android.util.Log
import androidx.annotation.RequiresPermission

import com.google.android.gms.maps.model.LatLng
import com.gox.base.base.BaseActivity
import com.gox.base.data.Constants

import java.io.IOException
import java.util.ArrayList
import java.util.Locale

import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient

object LocationUtils {

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun getLastKnownLocation(context: Context, mCallBack: LocationCallBack) {
        val mFusedLocation = getFusedLocationProviderClient(context)
        val locationResult = mFusedLocation.lastLocation
        locationResult.addOnCompleteListener { task ->
            if (task.isSuccessful && task.result != null)
                mCallBack.onSuccess(task.result!!)
        }.addOnFailureListener { e -> mCallBack.onFailure(e.localizedMessage) }
    }

    fun hasPermission(activity: BaseActivity<*>): Boolean {
        return activity.getPermissionUtil().hasPermission(activity, Constants.RequestPermission.PERMISSIONS_LOCATION)
    }

    fun getCurrentAddress(context: Context, currentLocation: LatLng): List<Address> {
        var addresses: List<Address> = ArrayList()
        val geoCoder: Geocoder
        try {
            if (Geocoder.isPresent()) {
                geoCoder = Geocoder(context, Locale.getDefault())
                addresses = geoCoder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 1)
                // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            }
        } catch (e: Exception) {
            Log.d("EXception", "EXception" + e.message)
        }

        return addresses
    }

    fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta)))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist *= 60.0 * 1.1515
        return dist
    }

    @Throws(IOException::class)
    fun getCountryCode(context: Context, currentLocation: LatLng): String {
        val addresses: List<Address>
        val geoCoder: Geocoder
        var countryCode = ""
        if (Geocoder.isPresent()) {
            geoCoder = Geocoder(context, Locale.getDefault())
            addresses = geoCoder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 1)
            if (addresses.isNotEmpty()) countryCode = addresses[0].countryCode
        }
        countryCode = if (TextUtils.isEmpty(countryCode)) "US" else countryCode
        return countryCode
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }
}