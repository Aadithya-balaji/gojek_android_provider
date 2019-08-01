package com.gox.partner.models

data class DisputeStatusModel(
        val error: List<Any?>? = listOf(),
        val message: String? = "",
        val responseData: ResponseData? = ResponseData(),
        val statusCode: String? = "",
        val title: String? = ""
) {
    data class DisputeStatusData(
            val comments: String? = "",
            val comments_by: String? = "",
            val company_id: Int? = 0,
            val created_at: String? = "",
            val dispute_name: String? = "",
            val dispute_title: Any? = Any(),
            val dispute_type: String? = "",
            val id: Int? = 0,
            val is_admin: Int? = 0,
            val provider_id: Int? = 0,
            val refund_amount: Int? = 0,
            val ride_request_id: Int? = 0,
            val status: String? = "",
            val user_id: Int? = 0
    )
}