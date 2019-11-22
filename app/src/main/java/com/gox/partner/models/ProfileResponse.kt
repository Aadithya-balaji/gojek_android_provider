package com.gox.partner.models

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
        var error: List<Any> = listOf(),
        var message: String = "",
        @SerializedName("responseData")
        var profileData: ProfileResponseData = ProfileResponseData(),
        var statusCode: String = "",
        var title: String = ""
)

data class ProfileResponseData(
        var activation_status: Int? = 0,
        var admin_id: Any = Any(),
        var city: ProfileCity = ProfileCity(),
        var city_id: Int? = 0,
        var country: Country = Country(),
        var country_code: String = "",
        var country_id: Int? = 0,
        var currency: Any = Any(),
        var currency_symbol: String = "",
        var current_location: Any = Any(),
        var device_id: Any = Any(),
        var device_token: Any = Any(),
        var device_type: Any = Any(),
        var email: String = "",
        var first_name: String = "",
        var gender: Any = Any(),
        var id: Int? = 0,
        var is_assigned: Int? = 0,
        var is_bankdetail: Int? = 0,
        var is_document: Int? = 0,
        var is_online: Int? = 0,
        var is_service: Int? = 0,
        var language: String = "",
        var last_name: String = "",
        var latitude: Double? = 0.0,
        var login_by: String = "",
        var longitude: Double? = 0.0,
        var mobile: String = "",
        var otp: Any = Any(),
        var payment_gateway_id: Any = Any(),
        var payment_mode: String = "",
        var picture: String = "",
        var qrcode_url: String = "",
        var rating: Double? = 0.0,
        var referal_count: Int? = 0,
        var referral: Referral = Referral(),
        var referral_unique_id: String = "",
        var service: ProfileService = ProfileService(),
        var social_unique_id: Any = Any(),
        var state: Any = Any(),
        var state_id: Any = Any(),
        var status: String = "",
        var stripe_cust_id: Any = Any(),
        var wallet_balance: Double? = 0.0,
        var zone_id: Any = Any()
)

data class Referral(
        var referral_amount: String = "",
        var referral_code: String = "",
        var referral_count: Int? = 0,
        var user_referral_amount: Int? = 0,
        var user_referral_count: Int? = 0
)

data class Country(
        var country_code: String = "",
        var country_currency: Any = Any(),
        var country_name: String = "",
        var country_phonecode: String = "",
        var country_symbol: Any = Any(),
        var id: Int? = 0,
        var status: Any = Any(),
        var timezone: Any = Any()
)

data class ProfileCity(
        var city_name: String = "",
        var country_id: Int? = 0,
        var id: Int = 0,
        var state_id: Int? = 0,
        var status: Any = Any()
)

data class ProfileService(
        var admin_service_id: Int? = 0,
        var base_fare: Double? = 0.0,
        var category_id: Int? = 0,
        var company_id: Int? = 0,
        var id: Int? = 0,
        var per_miles: Double? = 0.0,
        var per_mins: Double? = 0.0,
        var provider_id: Int? = 0,
        var provider_vehicle_id: Int? = 0,
        var ride_delivery_id: Int? = 0,
        var service_id: Any = Any(),
        var status: String = "",
        var sub_category_id: Any = Any()
)