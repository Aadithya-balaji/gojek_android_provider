package com.xjek.xuberservice.model

data class UpdateRequest(
        val error: List<Any?>? = listOf(),
        val message: String? = "",
        val responseData: ResponseData? = ResponseData(),
        val statusCode: String? = "",
        val title: String? = ""
) {
    data class ResponseData(
            val admin_id: Any? = Any(),
            val after_comment: Any? = Any(),
            val after_image: Any? = Any(),
            val allow_description: Any? = Any(),
            val allow_image: Any? = Any(),
            val assigned_at: String? = "",
            val before_image: Any? = Any(),
            val booking_id: String? = "",
            val cancel_reason: Any? = Any(),
            val cancelled_by: Any? = Any(),
            val city_id: Int? = 0,
            val company_id: Int? = 0,
            val country_id: Any? = Any(),
            val currency: String? = "",
            val distance: Int? = 0,
            val finished_at: String? = "",
            val id: Int? = 0,
            val is_scheduled: String? = "",
            val otp: String? = "",
            val paid: Any? = Any(),
            val payment_mode: String? = "",
            val price: Any? = Any(),
            val promocode_id: Int? = 0,
            val provider_id: Int? = 0,
            val provider_rated: Int? = 0,
            val quantity: Any? = Any(),
            val request_type: String? = "",
            val route_key: String? = "",
            val s_address: String? = "",
            val s_latitude: Double? = 0.0,
            val s_longitude: Double? = 0.0,
            val schedule_at: Any? = Any(),
            val service_id: Int? = 0,
            val started_at: String? = "",
            val status: String? = "",
            val surge: Int? = 0,
            val timezone: String? = "",
            val travel_time: String? = "",
            val unit: String? = "",
            val use_wallet: Int? = 0,
            val user: User? = User(),
            val user_id: Int? = 0,
            val user_rated: Int? = 0
    ) {
        data class User(
                val city_id: Int? = 0,
                val country_code: String? = "",
                val country_id: Int? = 0,
                val created_at: String? = "",
                val currency_symbol: String? = "",
                val email: String? = "",
                val first_name: String? = "",
                val gender: String? = "",
                val id: Int? = 0,
                val language: String? = "",
                val last_name: String? = "",
                val latitude: Any? = Any(),
                val login_by: String? = "",
                val longitude: Any? = Any(),
                val mobile: String? = "",
                val payment_mode: String? = "",
                val picture: Any? = Any(),
                val rating: String? = "",
                val state_id: Int? = 0,
                val status: Int? = 0,
                val user_type: String? = "",
                val wallet_balance: Int? = 0
        )
    }
}