package com.gox.xuberservice.model

data class CancelRequestModel(
        val error: List<Any?>? = listOf(),
        val message: String? = "",
        val responseData: List<Any?>? = listOf(),
        val statusCode: String? = "",
        val title: String? = ""
)