package com.gox.base.data

import android.Manifest
import android.content.Context
import android.util.Base64
import com.gox.base.BuildConfig
import com.gox.base.base.BaseApplication
import java.nio.charset.Charset

object Constants {

    const val DEFAULT_ZOOM = 15.0f
    const val CUSTOM_PREFERENCE: String = "BaseConfigSetting"

    const val SERVICE_ID = "service_id"
    const val CATEGORY_ID = "category_id"
    const val PROVIDER_TRANSPORT_VEHICLE = "provider_transport_vehicle"
    const val TRANSPORT_VEHICLES = "transport_vehicles"
    const val PROVIDER_ORDER_VEHICLE = "provider_order_vehicle"
    const val DOCUMENT_NAME = "document_name"
    const val DOCUMENT_TYPE = "document_type"

    const val APP_REQUEST_CODE = 99
    const val COUNTRY_LIST_REQUEST_CODE = 100
    const val CITY_LIST_REQUEST_CODE = 102
    const val DOCUMENT_UPLOAD_REQUEST_CODE = 12

    const val TEMP_FILE_NAME = "Gojek_Provider"
    const val TYPE_PROVIDER = "provider"
    const val currency = "$"

    var privacyPolicyUrl: String = ""
    var termsUrl: String = ""
    const val PATTERN_MATCH_ZERO="[0]*[.]?[0]*"

    object RoomConstants {
        @JvmField
        var COMPANY_ID: String = String(Base64.decode(BuildConfig.SALT_KEY, Base64.DEFAULT), Charset.defaultCharset())
        var CITY_ID: Int? = PreferencesHelper.get(PreferencesKey.CITY_ID, 0)
        var TRANSPORT_REQ_ID: Int? = PreferencesHelper.get(PreferencesKey.TRANSPORT_REQ_ID, 0)
        var SERVICE_REQ_ID: Int? = PreferencesHelper.get(PreferencesKey.SERVICE_REQ_ID, 0)
        var ORDER_REQ_ID: Int? = PreferencesHelper.get(PreferencesKey.ORDER_REQ_ID, 0)
    }

    object RequestCode {
        const val PERMISSIONS_CODE_LOCATION = 1001
        const val PERMISSION_CODE_CAMERA = 1003
        const val ADD_CARD = 125
        const val SELECTED_CARD = 1004
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
        var APP_BASE_URL: String = BaseApplication.run {
            getBaseApplicationContext.getSharedPreferences(CUSTOM_PREFERENCE,
                    Context.MODE_PRIVATE).getString(PreferencesKey.BASE_URL, BuildConfig.BASE_URL)
        }!!
        var TAXI_BASE_URL: String = BaseApplication.run {
            getBaseApplicationContext.getSharedPreferences(CUSTOM_PREFERENCE,
                    Context.MODE_PRIVATE).getString(PreferencesKey.TRANSPORT_URL, BuildConfig.BASE_URL)
        }!!
        var ORDER_BASE_URL: String = BaseApplication.run {
            getBaseApplicationContext.getSharedPreferences(CUSTOM_PREFERENCE,
                    Context.MODE_PRIVATE).getString(PreferencesKey.ORDER_URL, BuildConfig.BASE_URL)
        }!!
        var SERVICE_BASE_URL: String = BaseApplication.run {
            getBaseApplicationContext.getSharedPreferences(CUSTOM_PREFERENCE,
                    Context.MODE_PRIVATE).getString(PreferencesKey.SERVICE_URL, BuildConfig.BASE_URL)
        }!!
    }

    object RoomName {
        var COMMON_ROOM_NAME: String = "joinCommonRoom"
        var STATUS: String = "socketStatus"
        var NEW_REQ: String = "newRequest"
        var RIDE_REQ: String = "rideRequest"
        var SERVICE_REQ: String = "serveRequest"
        var ORDER_REQ: String = "orderRequest"
        var CHATROOM: String = "send_message"
        var TRANSPORT_ROOM_NAME: String = "joinPrivateRoom"
        var SERVICE_ROOM_NAME: String = "joinPrivateRoom"
        var ORDER_ROOM_NAME: String = "joinPrivateRoom"
        var UPDATELOCATION: String = "updateLocation"
        var JOIN_ROOM_NAME: String = "joinPrivateChatRoom"
        var ON_MESSAGE_RECEIVE: String = "new_message"
    }

    object RoomId {
        @JvmField
        var COMMON_ROOM: String = "room_${RoomConstants.COMPANY_ID}_${RoomConstants.CITY_ID}"
        var TRANSPORT_ROOM: String = "room_${RoomConstants.COMPANY_ID}_R${RoomConstants.TRANSPORT_REQ_ID}_TRANSPORT"
        var SERVICE_ROOM: String = "room_${RoomConstants.COMPANY_ID}_R${RoomConstants.SERVICE_REQ_ID}_SERVICE"
        var ORDER_ROOM: String = "room_${RoomConstants.COMPANY_ID}_R${RoomConstants.ORDER_REQ_ID}_ORDER"

        fun getCommonRoom(cityID:Int):String{
            return "room_${RoomConstants.COMPANY_ID}_${cityID}"
        }

        fun getTransportRoom(reqID:Int):String{
            return "room_${RoomConstants.COMPANY_ID}_R${reqID}_TRANSPORT"
        }

        fun getServiceRoom(reqID:Int):String{
            return "room_${RoomConstants.COMPANY_ID}_R${reqID}_SERVICE"
        }

        fun getOrderRoom(reqID:Int):String{
            return "room_${RoomConstants.COMPANY_ID}_R${reqID}_ORDER"
        }
    }

    object ModuleTypes {
        const val TRANSPORT = "TRANSPORT"
        const val SERVICE = "SERVICE"
        const val ORDER = "ORDER"
    }

    object ProviderStatus {
        const val PENDING = "pending"
        const val WAITING = "waiting"
        const val LOW_BALANCE = "low_balance"
    }

    object FareType {
        const val DISTANCE_TIME = "DISTANCETIME"
        const val FIXED = "FIXED"
        const val HOURLY = "HOURLY"
    }

    object BroadCastTypes {
        const val TRANSPORT_BROADCAST = "TRANSPORT_BROADCAST"
        const val SERVICE_BROADCAST = "SERVICE_BROADCAST"
        const val ORDER_BROADCAST = "ORDER_BROADCAST"
        const val BASE_BROADCAST = "BASE_BROADCAST"
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
        const val PROCESSING = "PROCESSING"
        const val REACHED = "REACHED"
    }

    object PaymentMode {
        val CASH = "CASH"
        val CARD = "CARD"
    }

    object Common {
        const val ID = "id"
        const val ADMIN_SERVICE = "admin_service"
        const val OTP = "otp"
        const val METHOD = "_method"
        const val SERVICE_ID = "service_id"
    }

    object XUberProvider {
        const val START = "START"
        const val CANCEL = "Cancel"
        const val STATUS = "status"
        const val RATING = "rating"
        const val COMMENT = "comment"
        const val REASON = "reason"
        const val EXTRA_CHARGE = "extra_charge"
        const val EXTRA_CHARGE_NOTES = "extra_charge_notes"
    }

    object Dispute {
        const val DISPUTE_TYPE = "dispute_type"
        const val DISPUTE_NAME = "dispute_name"
        const val USER_ID = "user_id"
        const val PROVIDER_ID = "provider_id"
        const val REQUEST_ID = "id"
        const val STORE_ID = "store_id"
        const val DISPUTE_ID = "dispute_id"
    }

    object Chat {
        const val USER_ID = "USER_ID"
        const val REQUEST_ID = "REQUEST_ID"
        const val PROVIDER_ID = "PROVIDER_ID"
        const val USER_NAME = "USER_NAME"
        const val ADMIN_SERVICE = "ADMIN_SERVICE"
        const val PROVIDER_NAME = "PROVIDER_NAME"
    }
}