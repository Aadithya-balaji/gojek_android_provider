package com.gox.partner.network

object WebApiConstants {

    object Login {
        const val COUNTRY_CODE = "country_code"
        const val MOBILE = "mobile"
        const val EMAIL = "email"
        const val PASSWORD = "password"
        const val DEVICE_TOKEN = "device_token"
        const val DEVICE_TYPE = "device_type"
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
        const val CONFIRM_PASSWORD = "password_confirmation"
    }

    object ValidateUser {
        const val EMAIL = "email"
        const val PHONE = "mobile"
        const val COUNTRY_CODE = "country_code"
    }

    object SignUp {
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
        const val REFERRAL_CODE = "referral_code"
    }

    object AddWallet {
        const val AMOUNT = "amount"
        const val CARD_ID = "card_id"
        const val USER_TYPE = "user_type"
        const val PAYMENT_MODE = "payment_mode"
    }

    object addCard {
        const val STRIP_TOKEN = "stripe_token"
    }

    object AddService {
        const val ID = "id"
        const val VEHICLE_ID = "vehicle_id"
        const val VEHICLE_YEAR = "vehicle_year"
        const val VEHICLE_MAKE = "vehicle_make"
        const val VEHICLE_MODEL = "vehicle_model"
        const val VEHICLE_NO = "vehicle_no"
        const val CHILD_SEAT = "child_seat"
        const val WHEEL_CHAIR = "wheel_chair"
        const val VEHICLE_COLOR = "vehicle_color"
        const val ADMIN_SERVICE = "admin_service"
        const val CATEGORY_ID = "category_id"
        const val SUB_CATEGORY_ID = "sub_category_id"
        const val VEHICLE_IMAGE = "vechile_image"
        const val PICTURE = "picture"
        const val PICTURE1 = "picture1"
    }
}