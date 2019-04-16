package com.xjek.provider.models

data class NotificationResponse(
        val error: List<Any>,
        val message: String,
        val responseData: NotificationResponseData,
        val statusCode: String,
        val title: String
)

data class NotificationResponseData(
        val current_page: Int,
        val `data`: List<NotificationData>,
        val first_page_url: String,
        val from: Int,
        val last_page: Int,
        val last_page_url: String,
        val next_page_url: Any,
        val path: String,
        val per_page: Int,
        val prev_page_url: Any,
        val to: Int,
        val total: Int
)

data class NotificationData(
        val company_id: Int,
        val created_at: String,
        val created_by: Int,
        val created_type: String,
        val deleted_by: Any,
        val deleted_type: Any,
        val description: String,
        val expiry_date: String,
        val id: Int,
        val image: String,
        val modified_by: Int,
        val modified_type: String,
        val notify_type: String,
        val service: String,
        val status: String,
        val title: String,
        val updated_at: String
)