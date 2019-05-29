package com.gox.partner.models

import java.io.Serializable

data class CityListResponse(
        val error: List<Any>,
        val message: String,
        val responseData: List<CityListResponseData>,
        val statusCode: String,
        val title: String
) : Serializable

data class CityListResponseData(
        val city_name: String,
        val id: Int,
        val state_id: Int,
        val status: Any
) : Serializable