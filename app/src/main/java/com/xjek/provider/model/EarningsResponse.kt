package com.xjek.provider.model

data class EarningsResponse(
        var error: List<Any> = listOf(),
        var message: String = "",
        var responseData: ResponseData = ResponseData(),
        var statusCode: String = "",
        var title: String = ""
)

data class ResponseData(
        var earning: String = "",
        var type: String = ""
)