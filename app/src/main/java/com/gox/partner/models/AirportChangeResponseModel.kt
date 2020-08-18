package com.gox.partner.models

data class AirportChangeResponseModel(
    val error: List<Any> = listOf(),
    val message: String = "",
    val responseData: ResponseData = ResponseData(),
    val statusCode: String = "",
    val title: String = ""
) {
    data class ResponseData(
        val status: Int = 0
    )
}