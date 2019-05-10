package com.xjek.provider.model

data class Test(
        val error: List<Any?>? = listOf(),
        val message: String? = "",
        val responseData: ResponseData? = ResponseData(),
        val statusCode: String? = "",
        val title: String? = ""
) {
    data class ResponseData(
            val service: List<Service?>? = listOf(),
            val total_records: Int? = 0,
            val type: String? = ""
    ) {
        data class Service(
                val assigned_at: String? = "",
                val booking_id: String? = "",
                val company_id: Int? = 0,
                val id: Int? = 0,
                val payment: Payment? = Payment(),
                val provider_id: Int? = 0,
                val s_address: String? = "",
                val service: Service? = Service(),
                val service_id: Int? = 0,
                val started_at: String? = "",
                val static_map: String? = "",
                val status: String? = "",
                val user: User? = User(),
                val user_id: Int? = 0
        ) {
            data class Payment(
                    val id: Int? = 0,
                    val service_request_id: Int? = 0,
                    val total: Double? = 0.0
            )

            data class User(
                    val first_name: String? = "",
                    val id: Int? = 0,
                    val last_name: String? = "",
                    val rating: String? = ""
            )

            data class Service(
                    val id: Int? = 0,
                    val service_category_id: Int? = 0,
                    val service_name: String? = ""
            )
        }
    }
}