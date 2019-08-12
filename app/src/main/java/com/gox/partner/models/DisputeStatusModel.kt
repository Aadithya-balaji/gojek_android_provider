package com.gox.partner.models

data class DisputeStatusModel(
        val error: List<Any>,
        val message: String,
        val responseData:DisputeState,
        val statusCode: String,
        val title: String
)

data class DisputeState(
        val comments: Any,
        val comments_by: String,
        val created_at: String,
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