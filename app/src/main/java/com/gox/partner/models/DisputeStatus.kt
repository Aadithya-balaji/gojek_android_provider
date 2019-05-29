package com.gox.partner.models

data class DisputeStatus(
        val error: List<Any?>? = listOf(),
        val message: String? = "",
        val responseData: List<ResponseData?>? = listOf(),
        val statusCode: String? = "",
        val title: String? = ""
) {
    data class ResponseData(
            val dispute_name: String? = "",
            val id: Int? = 0,
            val service: String? = ""
    )
}