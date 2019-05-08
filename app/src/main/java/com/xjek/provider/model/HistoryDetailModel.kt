package com.xjek.provider.model

data class HistoryDetailModel(
        val error: List<Any>,
        val message: String,
        val responseData: HistoryDetailResponseData,
        val statusCode: String,
        val title: String
)

data class HistoryDetailResponseData(
        val transport: HistoryDetailTransport,
        val type: String
)

data class HistoryDetailTransport(
        val assigned_at: String,
        val assigned_time: String,
        val booking_id: String,
        val calculator: String,
        val cancel_reason: Any,
        val cancelled_by: Any,
        val city_id: Int,
        val company_id: Int,
        val country_id: Any,
        val created_at: String,
        val currency: Any,
        val d_address: String,
        val d_latitude: Double,
        val d_longitude: Double,
        val destination_log: String,
        val dispute: Dispute,
        val distance: Double,
        val finished_at: String,
        val finished_time: String,
        val id: Int,
        val is_scheduled: String,
        val is_track: String,
        val lost_item: LostItem,
        val otp: String,
        val paid: Int,
        val payment: HistoryDetailPayment,
        val payment_mode: String,
        val peak_hour_id: Any,
        val promocode_id: Int,
        val provider: HistoryDetailProvider,
        val provider_id: Int,
        val provider_rated: Int,
        val provider_service_id: Int,
        val provider_vehicle: ProviderVehicle,
        val provider_vehicle_id: Int,
        val request_type: String,
        val ride: HistoryDetailRide,
        val ride_delivery_id: Int,
        val route_key: String,
        val s_address: String,
        val s_latitude: Double,
        val s_longitude: Double,
        val schedule_at: Any,
        val schedule_time: String,
        val service_type: HistoryDetailServiceType,
        val started_at: String,
        val started_time: String,
        val static_map: String,
        val status: String,
        val surge: Int,
        val timezone: String,
        val track_distance: Double,
        val track_latitude: Double,
        val track_longitude: Double,
        val travel_time: String,
        val unit: String,
        val use_wallet: Int,
        val user_id: Int,
        val user_rated: Int,
        val vehicle_type: String
)

data class LostItem(
        val id: Int,
        val ride_request_id: Int,
        val company_id: Int,
        val user_id: Int,
        val lost_item_name: String,
        val comments: String,
        val comments_by: String,
        val status: String,
        val is_admin: Int,
        val created_type: String,
        val created_by: Int,
        val modified_type: String,
        val modified_by: Int,
        val deleted_type: String,
        val deleted_by: String,
        val created_at: String,
        val updated_at: String,
        val deleted_at: String)


data class HistoryDetailPayment(
        val card: Int,
        val cash: Int,
        val commision: Double,
        val commision_percent: Int,
        val company_id: Int,
        val discount: Int,
        val discount_percent: Int,
        val distance: Double,
        val fixed: Double,
        val fleet: Int,
        val fleet_id: Any,
        val fleet_percent: Int,
        val hour: Int,
        val id: Int,
        val is_partial: Any,
        val minute: Int,
        val payable: Int,
        val payment_id: Any,
        val payment_mode: Any,
        val peak_amount: Int,
        val peak_comm_amount: Int,
        val promocode_id: Any,
        val provider_id: Int,
        val provider_pay: Double,
        val ride_request_id: Int,
        val round_of: Double,
        val tax: Double,
        val tax_percent: Int,
        val tips: Int,
        val toll_charge: Double,
        val total: Double,
        val total_waiting_time: Int,
        val user_id: Int,
        val waiting_amount: Double,
        val waiting_comm_amount: Double,
        val wallet: Double
)

data class ProviderVehicle(
        val company_id: Int,
        val id: Int,
        val picture: Any,
        val picture1: Any,
        val provider_id: Int,
        val vechile_image: Any,
        val vehicle_color: Any,
        val vehicle_make: Any,
        val vehicle_model: String,
        val vehicle_no: String,
        val vehicle_service_id: Int,
        val vehicle_year: Int
)

data class HistoryDetailServiceType(
        val admin_service_id: Int,
        val base_fare: String,
        val category_id: String,
        val company_id: Int,
        val id: Int,
        val per_miles: String,
        val per_mins: String,
        val provider_id: Int,
        val provider_vehicle_id: Int,
        val ride_delivery_id: Int,
        val service_id: Any,
        val status: String,
        val sub_category_id: Any
)

data class HistoryDetailRide(
        val capacity: Int,
        val company_id: Int,
        val id: Int,
        val ride_type_id: Int,
        val status: Int,
        val vehicle_image: String,
        val vehicle_marker: String,
        val vehicle_name: String,
        val vehicle_type: String
)

data class Dispute(
        val comments: Any,
        val comments_by: String,
        val company_id: Int,
        val created_at: Any,
        val dispute_name: String,
        val dispute_title: String,
        val dispute_type: String,
        val id: Int,
        val is_admin: Int,
        val provider_id: Int,
        val refund_amount: Int,
        val ride_request_id: Int,
        val status: String,
        val user_id: Int
)

data class HistoryDetailProvider(
        val activation_status: Int,
        val admin_id: Any,
        val city_id: Int,
        val country_code: String,
        val country_id: Any,
        val currency: Any,
        val currency_symbol: Any,
        val device_id: Any,
        val device_token: String,
        val device_type: String,
        val email: String,
        val first_name: String,
        val gender: String,
        val id: Int,
        val is_assigned: Int,
        val is_bankdetail: Int,
        val is_document: Int,
        val is_online: Int,
        val is_service: Int,
        val language: String,
        val last_name: String,
        val latitude: Double,
        val login_by: String,
        val longitude: Double,
        val mobile: String,
        val otp: Any,
        val payment_gateway_id: Any,
        val payment_mode: String,
        val picture: String,
        val qrcode_url: String,
        val rating: Double,
        val referal_count: Int,
        val referral_unique_id: String,
        val social_unique_id: Any,
        val state_id: Any,
        val status: String,
        val stripe_cust_id: String,
        var wallet_balance: Double = 0.0,
        val zone_id: Any
)