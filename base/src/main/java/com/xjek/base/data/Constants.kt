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
        const val PERMISSION_CODE_CAMERA = 1003
    }

    object RequestPermission {
        val PERMISSIONS_LOCATION = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val PERMISSION_CAMERA = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        )
    }

    object ModuleTypes {
        const val TRANSPORT = "TRANSPORT"
        const val SERVICE = "SERVICE"
        const val ORDER = "ORDER"
    }

    object BroadCastTypes {
        const val TRANSPORT_BROADCAST = "TRANSPORT_BROADCAST"
        const val SERVICE_BROADCAST = "SERVICE_BROADCAST"
        const val ORDER_BROADCAST = "ORDER_BROADCAST"
        const val BASE_BROADCAST = "BASE_BROADCAST"
    }

    object ProviderStatus {
        const val ACTIVE = "ACTIVE"
        const val RIDING = "RIDING"
        const val OFFLINE = "OFFLINE"
    }

    object RideStatus {
        const val SEARCHING = "SEARCHING"
        const val SCHEDULED = "SCHEDULED"
        const val CANCELLED = "CANCELLED"
        const val ACCEPTED = "ACCEPTED"
        const val STARTED = "STARTED"
        const val ARRIVED = "ARRIVED"
        const val PICKED_UP = "PICKEDUP"
        const val DROPPED = "DROPPED"
        const val COMPLETED = "COMPLETED"
        const val PAYMENT = "PAYMENT"
    }

    object Common {
        const val ID = "id"
        const val RATING = "rating"
        const val COMMENT = "comment"
        const val ADMIN_SERVICE_ID = "admin_service_id"
        const val OTP = "otp"
    }

    object XuperProvider {
        const val AFTER_IMAGE = "after_picture"
        const val BEFORE_IMAGE = "before_picture"
        const val EXTRA_CHARGE = "extra_charge"
        const val EXTRA_CHARGE_NOTES = "extra_charge_notes"

    }
}