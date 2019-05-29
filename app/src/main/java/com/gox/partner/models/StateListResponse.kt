package com.gox.partner.models

import java.io.Serializable

data class StateListResponse(
        val error: List<Any>,
        val message: String,
        val responseData: List<StateListResponseData>,
        val statusCode: String,
        val title: String
) : Serializable

data class StateListResponseData(
        val country_id: Int,
        val id: Int,
        val state_name: String,
        val status: Any,
        val timezone: Any
) : Serializable