package com.gox.foodservice.model

import java.io.Serializable

data class FoodieRatingRequestModel(
        var error: List<Any> = listOf(),
        var message: String = "",
        var responseData: List<ResponseData> = listOf(),
        var statusCode: String = "",
        var title: String = ""
) : Serializable
