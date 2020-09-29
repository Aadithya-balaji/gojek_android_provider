package com.bee.courierservice.model

data class CourierRatingModel(
        val error: List<Any?>? = listOf(),
        val message: String? = "",
        val responseData: List<Any?>? = listOf(),
        val statusCode: String? = "",
        val title: String? = ""
)