package com.xjek.provider.model

data class EarningsResponse(
        var error: List<Any> = listOf(),
        var message: String = "",
        var responseData: EarningsResponseData = EarningsResponseData(),
        var statusCode: String = "",
        var title: String = ""
)

data class EarningsResponseData(
        var today: String = "",
        var week: String = "",
        var month: String = ""
)