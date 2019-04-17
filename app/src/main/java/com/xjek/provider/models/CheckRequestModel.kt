package com.xjek.provider.models

data class CheckRequestModel(
        var error: List<Any?>?,
        var message: String?,
        var responseData: ResponseData?,
        var statusCode: String?,
        var title: String?
)

data class ResponseData(
        var account_status: String?,
        var provider_details: ProviderDetails?,
        var reasons: List<Any?>?,
        var referral_amount: String?,
        var referral_count: String?,
        var referral_total_amount: Int?,
        var referral_total_count: String?,
        var requests: List<Request?>?,
        var ride_otp: String?,
        var service_status: String?
)

data class Request(
        var admin_service_id: Int?,
        var company_id: Int?,
        var created_at: String?,
        var id: Int?,
        var provider_id: Any?,
        var request: RequestX?,
        var request_id: Int?,
        var schedule_at: Any?,
        var service: Service?,
        var status: String?,
        var time_left_to_respond: Int?,
        var user: User?,
        var user_id: Int?
)

data class User(
        var city_id: Int?,
        var country_code: String?,
        var country_id: Int?,
        var created_at: String?,
        var currency_symbol: String?,
        var email: String?,
        var first_name: String?,
        var gender: String?,
        var id: Int?,
        var language: String?,
        var last_name: String?,
        var latitude: Any?,
        var login_by: String?,
        var longitude: Any?,
        var mobile: String?,
        var payment_mode: String?,
        var picture: Any?,
        var rating: String?,
        var state_id: Int?,
        var status: Int?,
        var user_type: String?,
        var wallet_balance: Int?
)

data class RequestX(
        var assigned_at: String?,
        var assigned_time: String?,
        var booking_id: String?,
        var city_id: Int?,
        var company_id: Int?,
        var created_at: String?,
        var currency: Any?,
        var d_address: String?,
        var d_latitude: String?,
        var d_longitude: String?,
        var destination_log: String?,
        var distance: Int?,
        var finished_time: String?,
        var id: Int?,
        var is_track: String?,
        var otp: Int?,
        var payment_mode: String?,
        var promocode_id: Int?,
        var provider_service_id: String?,
        var ride_delivery_id: String?,
        var route_key: String?,
        var s_address: String?,
        var s_latitude: String?,
        var s_longitude: String?,
        var schedule_time: String?,
        var started_time: String?,
        var status: String?,
        var timezone: String?,
        var track_distance: Int?,
        var track_latitude: String?,
        var track_longitude: String?,
        var unit: String?,
        var user_id: Int?
)

data class Service(
        var admin_service_name: String?,
        var base_url: String?,
        var display_name: Any?,
        var id: Int?,
        var status: Int?
)

data class ProviderDetails(
        var activation_status: Int?,
        var admin_id: Any?,
        var city_id: Int?,
        var country_code: String?,
        var country_id: Int?,
        var currency: Any?,
        var currency_symbol: String?,
        var device_id: Any?,
        var device_token: String?,
        var device_type: String?,
        var email: String?,
        var first_name: String?,
        var gender: String?,
        var id: Int?,
        var is_assigned: Int?,
        var is_bankdetail: Int?,
        var is_document: Int?,
        var is_online: Int?,
        var is_service: Int?,
        var language: String?,
        var last_name: String?,
        var latitude: String?,
        var login_by: String?,
        var longitude: String?,
        var mobile: String?,
        var otp: Any?,
        var payment_gateway_id: Any?,
        var payment_mode: String?,
        var picture: Any?,
        var qrcode_url: String?,
        var rating: Int?,
        var referal_count: Int?,
        var referral_unique_id: String?,
        var service: ServiceX?,
        var social_unique_id: Any?,
        var state_id: Int?,
        var status: String?,
        var stripe_cust_id: Any?,
        var wallet_balance: Int?,
        var zone_id: Any?
)

data class ServiceX(
        var admin_service_id: Int?,
        var base_fare: String?,
        var category_id: Any?,
        var company_id: Int?,
        var id: Int?,
        var per_miles: String?,
        var per_mins: String?,
        var provider_id: Int?,
        var provider_vehicle_id: Int?,
        var ride_delivery_id: Int?,
        var service_id: Any?,
        var status: String?,
        var sub_category_id: Any?
)