package com.gox.partner.models

data class SendOTPResponse(
    val error: List<Any> = listOf(),
    val message: String = "",
    val responseData: List<Any> = listOf(),
    val statusCode: String = "",
    val title: String = ""
)