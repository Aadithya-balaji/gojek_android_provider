package com.xjek.base.data

import android.Manifest
import com.google.android.gms.maps.model.LatLng

object Constants {

    const val DEFAULT_ZOOM = 15.0f
    val DEFAULT_LOCATION = LatLng(-33.8523341, 151.2106085)

    object RequestCode {
        const val PERMISSIONS_CODE_LOCATION = 1001
        const val PERMISSIONS_CODE_FILE = 1002
        const val ORDER = "ORDER"
    }

    object RequestPermission {
        val PERMISSIONS_LOCATION = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val PERMISSIONS_FILE = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }


}