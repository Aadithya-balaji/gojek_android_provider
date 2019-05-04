package com.xjek.base.data

import android.Manifest
import android.util.Base64
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.xjek.base.BuildConfig
import java.nio.charset.Charset

object Constants {

    const val DEFAULT_ZOOM = 15.0f
    val DEFAULT_LOCATION = LatLng(-33.8523341, 151.2106085)
    var COMPANY_ID: String = String(Base64.decode(BuildConfig.SALT_KEY, Base64.DEFAULT), Charset.defaultCharset())
    var CITY_ID: Int = 0
    var isSocketFailed: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { postValue(false) }
    var REQ_ID:Int = 0

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

    object BaseUrl {
        @JvmField
        var APP_BASE_URL: String = BuildConfig.BASE_URL
        var TAXI_BASE_URL: String? = null
        var ORDER_BASE_URL: String? = null
        var SERVICE_BASE_URL: String? = null
    }

    object ROOM_NAME{
        var COMMON_ROOM_NAME:String = "joinCommonRoom"
        var STATUS:String = "socketStatus"
        var NEW_REQ:String = "newRequest"
        var RIDE_REQ:String = "rideRequest"
        var TRANSPORT_ROOM_NAME:String = "joinPrivateRoom"
        var SERVICE_ROOM_NAME:String?=null
    }

    object ROOM_ID{
        var COMMON_ROOM:String = "room_${COMPANY_ID}_$CITY_ID"
        var TRANSPORT_ROOM:String = "room_${COMPANY_ID}_${REQ_ID}_TRANSPORT"
        var SERVICE_ROOM:String = ""
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
    }

    object Common {
        const val ID = "id"
        const val RATING = "rating"
        const val COMMENT = "comment"
        const val ADMIN_SERVICE_ID = "admin_service_id"
        const val OTP = "otp"
        const val METHOD = "_method"
        const val SERVICEID = "service_id"
    }

    object XuperProvider {
        const val AFTER_IMAGE = "after_picture"
        const val BEFORE_IMAGE = "before_picture"
        const val EXTRA_CHARGE = "extra_charge"
        const val EXTRA_CHARGE_NOTES = "extra_charge_notes"
        const val PAYMENT = "PAYMENT"
        const val START = "START"
        const val CANCEL = "CANCEL"
        const val STATUS = "status"
        const val REQCANCEL = "cancel"
        const val RATING = "rating"
        const val COMMENT = "comment"
    }

    object Reasons {
        const val TRANSPORT = "TRANSPORT"
        const val SERVICE = "SERVICE"
        const val ORDER = "ORDER"
    }
}