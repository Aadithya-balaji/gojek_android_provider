package com.gox.partner.models

data class RentalOutsationResponse(
        var error: List<Any> = listOf(),
        var message: String = "",
        var statusCode: String = ""
)