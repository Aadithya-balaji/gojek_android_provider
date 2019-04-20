package com.xjek.provider.network

object WebApiConstants {

    const val SALT_KEY = "salt_key"
    const val REQUESTED_WITH = "X-Requested-With"
    const val HTTP_REQUEST = "XMLHttpRequest"
    const val AUTHORIZATION = "Authorization"
    const val DEVICE_TYPE = "device_type"
    const val DEVICE_TOKEN = "device_token"
    const val LOGIN_BY = "login_by"
    const val EMAIL = "email"
    const val PASSWORD = "password"
    const val FIRST_NAME = "first_name"
    const val LAST_NAME = "last_name"
    const val GENDER = "gender"
    const val COUNTRY_CODE = "country_code"
    const val MOBILE = "mobile"
    const val CONFIRM_PASSWORD = "password_confirmation"
    const val COUNTRY_ID = "country_id"
    const val STATE_ID = "state_id"
    const val CITY_ID = "city_id"
    const val PHONE_NUMBER="mobile"
    const val LIMIT="limit"
    const val OFFSET="offset"

    object Login {
        const val COUNTRY_CODE = "country_code"
        const val MOBILE = "mobile"
        const val EMAIL = "email"
        const val PASSWORD = "password"
    }

    object SocialLogin {
        const val DEVICE_TYPE = "device_type"
        const val DEVICE_TOKEN = "device_token"
        const val LOGIN_BY = "login_by"
        const val SOCIAL_UNIQUE_ID = "social_unique_id"
    }

    object ForgotPassword {
        const val ACCOUNT_TYPE = "account_type"
        const val COUNTRY_CODE = "country_code"
        const val MOBILE = "mobile"
        const val EMAIL = "email"
    }

    object ResetPassword {
        const val ACCOUNT_TYPE = "account_type"
        const val COUNTRY_CODE = "country_code"
        const val USERNAME = "username"
        const val OTP = "otp"
        const val PASSWORD = "password"
        const val PASSWORD_CONFIRMATION = "password_confirmation"
    }

    object ChangePassword {
        const val OLD_PASSWORD = "old_password"
        const val PASSWORD = "password"
    }

    object ValidateUser {
        const val EMAIL = "email"
        const val PHONE = "mobile"
        const val COUNTRYCODE = "country_code"
    }

    object Signup {
        const val DEVICE_TYPE = "device_type"
        const val DEVICE_TOKEN = "device_token"
        const val LOGIN_BY = "login_by"
        const val FIRST_NAME = "first_name"
        const val LAST_NAME = "last_name"
        const val GENDER = "gender"
        const val COUNTRY_CODE = "country_code"
        const val MOBILE = "mobile"
        const val CONFIRM_PASSWORD = "password_confirmation"
        const val PASSWORD = "password"
        const val COUNTRY_ID = "country_id"
        const val STATE_ID = "state_id"
        const val EMAIL = "email"
        const val CITY_ID = "city_id"
        const val SOCIAL_ID = "social_unique_id"
    }

    object UpdateProfile {
        const val FIRST_NAME = "first_name"
        const val LAST_NAME = "last_name"
        const val EMAIL = "email"
        const val MOBILE = "mobile"
        const val LANGUAGE = "language"
        const val CITY_ID = "city_id"
        const val COUNTRY_ID = "country_id"
        const val PICTURE = "picture"
    }


    object  AddWallet{
        const val AMOUNT="amount"
        const val CARD_ID="card_id"
        const val USER_TYPE="user_type"
        const val PAYMENT_MODE="payment_mode"
    }

    object  addCard{
        const val STRIP_TOKEN="stripe_token"
    }


    object  Availability{
        const val STATUS="status"
    }


    object AddService {
        const val VEHICLE_ID = "vehicle_id"
        const val VEHICLE_YEAR = "vehicle_year"
        const val VEHICLE_MAKE = "vehicle_make"
        const val VEHICLE_MODEL = "vehicle_model"
        const val VEHICLE_NO = "vehicle_no"
        const val VEHICLE_COLOR = "vehicle_color"
        const val ADMIN_SERVICE_ID = "admin_service_id"
        const val CATEGORY_ID = "category_id"
        const val SUB_CATEGORY_ID = "sub_category_id"
        const val VEHICLE_IMAGE = "vechile_image"
        const val PICTURE = "picture"
        const val PICTURE1 = "picture1"
    }

    object ListDocuments {
        const val TYPE = "type"
    }

    object AddDocument {
        const val DOCUMENT_NAME = "document_name"
        const val DOCUMENT_ID = "document_id"
        const val EXPIRES_AT = "expires_at"
        const val DOCUMENT_FRONTSIDE = "file[0]"
        const val DOCUMENT_BACKSIDE = "file[1]"
    }
}