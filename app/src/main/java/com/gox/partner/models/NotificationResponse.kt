package com.gox.partner.models

data class NotificationResponse(
        val error: List<Any> = listOf(),
        val message: String = "",
        val responseData: ResponseData = ResponseData(),
        val statusCode: String = "",
        val title: String = ""
) {
    data class ResponseData(
            val notification: Notification = Notification(),
            val total_records: Int = 0
    ) {
        data class Notification(
                val current_page: Int = 0,
                val `data`: List<Data> = listOf(),
                val first_page_url: String = "",
                val from: Int = 0,
                val last_page: Int = 0,
                val last_page_url: String = "",
                val next_page_url: String = "",
                val path: String = "",
                val per_page: Int = 0,
                val prev_page_url: Any? = Any(),
                val to: Int = 0,
                val total: Int = 0
        ) {
            data class Data(
                    val company_id: Int = 0,
                    val created_at: String = "",
                    val created_by: Int = 0,
                    val created_type: String = "",
                    val deleted_by: Any? = Any(),
                    val deleted_type: Any? = Any(),
                    val descriptions: String = "",
                    val expiry_date: String = "",
                    val id: Int = 0,
                    val image: String = "",
                    val modified_by: Int = 0,
                    val modified_type: String = "",
                    val notify_type: String = "",
                    val service: String = "",
                    val status: String = "",
                    val title: String = "",
                    val updated_at: String = ""
            )
        }
    }
}