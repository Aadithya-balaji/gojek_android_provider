package com.appoets.gojek.provider.models

import com.appoets.base.models.CommonResponse

data class ChangePasswordResponseModel(
        override val statusCode: String,
        override val title: String,
        override val message: String,
        override val error: List<Any>,
        val responseData: List<Any>) : CommonResponse