package com.xjek.provider.models

data class DisputeStatusModel(
        val responseData: DisputeStatusData,
        val error: List<Any>,
        val message: String,
        val statusCode: String,
        val title: String
)

data class DisputeStatusData(
        val comments: String,
        val comments_by: String,
        val company_id: Int,
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