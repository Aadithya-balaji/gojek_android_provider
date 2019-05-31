package com.gox.taxiservice.model

data class TaxiRatingResponse(
        var error: List<Any> = listOf(),
        var message: String = "",
        var responseData: List<Any> = listOf(),
        var statusCode: String = "",
        var title: String = ""
)