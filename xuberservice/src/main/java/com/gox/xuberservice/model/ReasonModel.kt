package com.gox.xuberservice.model

data class ReasonModel(
        val error: List<Any?>? = listOf(),
        val message: String? = "",
        val responseData: List<ResponseData?>? = listOf(),
        val statusCode: String? = "",
        val title: String? = ""
) {
    data class ResponseData(
            val created_by: Int? = 0,
            val created_type: String? = "",
            val deleted_by: Any? = Any(),
            val deleted_type: Any? = Any(),
            val id: Int? = 0,
            val modified_by: Int? = 0,
            val modified_type: String? = "",
            val reason: String? = "",
            val service: String? = "",
            val status: String? = "",
            val type: String? = ""
    )
}